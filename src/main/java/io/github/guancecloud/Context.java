package io.github.guancecloud;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Build connections between async-profiler with ddtrace
 */
public class Context {

    private static boolean loaded = false;
    private static final String GUANCE_PROF_NATIVE_LIB_IS_LOADED = "GUANCE_PROF_NATIVE_LIB_IS_LOADED";

    private static final int CONTEXT_SIZE = 64;
    private static final int PAGE_SIZE = 1024;
    private static final int SPAN_OFFSET = 0;
    private static final int ROOT_SPAN_OFFSET = 8;
    private static final int CHECKSUM_OFFSET = 16;
    private static final int DYNAMIC_TAGS_OFFSET = 24;

    private ByteBuffer[] contextStorage;
    private long[] contextBaseOffsets;

    private static final Unsafe UNSAFE;

    private static Context instance;

    private Context() {

    }

    public static synchronized Context getInstance() {
        if (instance != null) {
            return instance;
        }

        return instance = new Context();
    }




    static {
        Unsafe unsafe = null;
        String version = System.getProperty("java.version");
        if (version.startsWith("1.8")) {
            try {
                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                unsafe = (Unsafe) f.get(null);
            } catch (Exception ignore) { }
        }
        UNSAFE = unsafe;
    }

    private static ThreadLocal<Integer> TID = null;

    /**
     * Get thread identifier
     * @return int
     */
    public static int getTID() {
        if (!isLoaded()) {
            return 0;
        }
        if (TID == null) {
            TID = ThreadLocal.withInitial(Context::getThreadID0);
        }
        return TID.get();
    }


    /**
     * Test whether the native library is loaded.
     * @return boolean
     */
    public static boolean isLoaded() {
        if (loaded) {
            return true;
        }
        String env = System.getenv(GUANCE_PROF_NATIVE_LIB_IS_LOADED);
        loaded = (env != null && !env.equalsIgnoreCase("")
                && !env.equalsIgnoreCase("0")
                && !env.equalsIgnoreCase("false"));
        return loaded;
    }

    /**
     * record trace root for samples
     * @param rootSpanId root span id
     * @param endpoint endpoint
     * @param sizeLimit size limit
     * @return boolean
     */
    public boolean recordTraceRoot(long rootSpanId, String endpoint, int sizeLimit) {
        if (isLoaded()) {
            return recordTrace0(rootSpanId, endpoint, sizeLimit);
        }
        return true;
    }

    /**
     * set current span id and root span id for samples.
     * @param spanId span ID
     * @param rootSpanId root span ID
     */
    public void setContext(long spanId, long rootSpanId) {
        if (!isLoaded()) {
            return;
        }
        initializeContextStorage();
        int tid = getTID();
        if (UNSAFE != null) {
            setContextJDK8(tid, spanId, rootSpanId);
        } else {
            setContextByteBuffer(tid, spanId, rootSpanId);
        }
    }

    private void initializeContextStorage() {
        if (this.contextStorage == null) {
            if (!isLoaded()) {
                return;
            }
            int maxPages = getMaxContextPages0();
            if (maxPages > 0) {
                if (UNSAFE != null) {
                    contextBaseOffsets = new long[maxPages];
                    // be sure to choose an illegal address as a sentinel value
                    Arrays.fill(contextBaseOffsets, Long.MIN_VALUE);
                } else {
                    contextStorage = new ByteBuffer[maxPages];
                }
            }
        }
    }

    private void setContextJDK8(int tid, long spanId, long rootSpanId) {
        if (contextBaseOffsets == null) {
            return;
        }
        long pageOffset = getPageUnsafe(tid);
        int index = (tid % PAGE_SIZE) * CONTEXT_SIZE;
        long base = pageOffset + index;
        UNSAFE.putLong(base + SPAN_OFFSET, spanId);
        UNSAFE.putLong(base + ROOT_SPAN_OFFSET, rootSpanId);
        UNSAFE.putLong(base + CHECKSUM_OFFSET, spanId ^ rootSpanId);
    }

    private void setContextByteBuffer(int tid, long spanId, long rootSpanId) {
        if (contextStorage == null) {
            return;
        }
        ByteBuffer page = getPage(tid);
        int index = (tid % PAGE_SIZE) * CONTEXT_SIZE;
        page.putLong(index + SPAN_OFFSET, spanId);
        page.putLong(index + ROOT_SPAN_OFFSET, rootSpanId);
        page.putLong(index + CHECKSUM_OFFSET, spanId ^ rootSpanId);
    }

    private long getPageUnsafe(int tid) {
        int pageIndex = tid / PAGE_SIZE;
        long offset = contextBaseOffsets[pageIndex];
        if (offset == Long.MIN_VALUE) {
            if (isLoaded()) {
                contextBaseOffsets[pageIndex] = offset = getContextPageOffset0(tid);
            }
        }
        return offset;
    }

    private ByteBuffer getPage(int tid) {
        int pageIndex = tid / PAGE_SIZE;
        ByteBuffer page = contextStorage[pageIndex];
        if (page == null) {
            // the underlying page allocation is atomic so we don't care which view we have over it
            contextStorage[pageIndex] = page = getContextPage0(tid).order(ByteOrder.LITTLE_ENDIAN);
        }
        return page;
    }

    private static native boolean recordTrace0(long rootSpanId, String endpoint, int sizeLimit);

    private static native ByteBuffer getContextPage0(int tid);

    private static native int getMaxContextPages0();

    // this is only here because ByteBuffer.putLong splits its argument into 8 bytes
    // before performing the writing, which makes it more likely that writing the context
    // will be interrupted by the signal, leading to more rejected context values on samples
    // ByteBuffer is simpler and fit for purpose on modern JDKs
    private static native long getContextPageOffset0(int tid);

    private static native int getThreadID0();



}

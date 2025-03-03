package movies;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import datadog.trace.api.Trace;
import io.github.guancecloud.Context;
import spark.Request;
import spark.Response;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

import static spark.Spark.*;


class Fibonacci implements Runnable {


	@Trace(operationName = "newFibonacci", resourceName = "Fibonacci")
	static Fibonacci getInstance() {
		return new Fibonacci();
	}

	static Random random = new Random();

	static int i = 0;
	@Trace(operationName = "fibonacci", resourceName = "Fibonacci")
	static int fibonacci(int n) {
		if (n <= 2) {
			return 1;
		}
		return fibonacci1(n-1) + fibonacci2(n-2);
	}

	static int fibonacci1(int n) {
		if (n <= 2) {
			return 1;
		}

		if (random.nextInt(100000000) <= 1) {
			return fibonacci1(n-1) + fibonacci(n-2);
		} else {
			return fibonacci1(n-1) + fibonacci1(n-2);
		}
	}

	@Trace(operationName = "fibonacci2", resourceName = "Fibonacci")
	static int fibonacci2(int n) {
		if (n <= 2) {
			return 1;
		}
		return fibonacci1(n-1) + fibonacci1(n-2);
	}

	@Override
	@Trace(operationName = "run", resourceName = "Fibonacci")
	public void run() {
		System.out.println(fibonacci(45));
	}

}

class FileIO implements Runnable{

	@Trace(operationName = "createFile", resourceName = "FileIO")
	public static File createFile() throws IOException {
		return File.createTempFile("java_tmp", ".tmp", new File("./"));
	}


	@Trace(operationName = "writeFile", resourceName = "FileIO")
	public static void writeFile(File file) throws IOException {
		FileWriter writer = new FileWriter(file);
		writer.write(NetIO.body);
		writer.close();
	}

	@Trace(operationName = "readFile", resourceName = "FileIO")
	public static void readFile(File file) throws IOException {
		FileReader fileReader = new FileReader(file);

		char[] buf = new char[8];

		while (true) {
			int c = fileReader.read(buf);
			if (c < 0) {
				break;
			}
		}
		fileReader.close();
	}

	@Trace(operationName = "readWrite", resourceName = "FileIO")
	public static void readWrite() {
		try {
			File file = createFile();

			for (int i = 0; i < 1000; i++) {
				writeFile(file);
			}

			for (int i = 0; i < 1000; i++) {
				readFile(file);
			}

			if (!file.delete()) {
				System.out.println("unable to remove file: " + file.getName());
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void run() {
		for (int i = 0; i < 5; i++) {
			readWrite();
		}
	}
}

class NetIO implements Runnable {

	public static final String body = "银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。" +
			"银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"九派新闻从多名银行从业人员处了解到，取钱可以拿身份证代办，但像密码重置、银行卡挂失等业务，是需要本人办理。在老人不方便的情况下，可以向银行网点申请特事特办，让工作人员进行上门服务。\n" +
			"“银行工作人员为客户申请特事特办需要一定的审批时间，一般两到三天。到达客户指定上门地点后，工作人员会向思路还比较清晰的老人询问，是否确定要办这项业务、委托此人办理业务等问题，然后签订一份文件，再由代办人拿着两人的身份证来网点办理。”他说。\n" +
			"对于银行和储户之间的沟通问题，他表示自己所在支行有很多客户是老人，这类情况时有发生，上门服务的频次较高。";

	@Trace(operationName = "newNetIO", resourceName = "NetIO")
	static NetIO getInstance() {
		return new NetIO();
	}

	@Trace(operationName = "sendRequest", resourceName = "NetIO")
	public static synchronized void sendRequest() throws IOException {
		URL url = new URL("https://baijiahao.baidu.com/s?id=1766280179246190391&wfr=spider&for=pc");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		OutputStream outputStream = conn.getOutputStream();
		outputStream.write(body.getBytes(StandardCharsets.UTF_8));
		outputStream.close();

		System.out.println("http status code: " + conn.getResponseCode());
		System.out.println("http status message: " + conn.getResponseMessage());

		InputStream inputStream = conn.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		readResponse(bufferedReader);
		bufferedReader.close();
		inputStream.close();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Trace(operationName = "readResponse", resourceName = "NetIO")
	public static void readResponse(BufferedReader bufferedReader) throws IOException {
		String line;
		do {
			line = bufferedReader.readLine();
			System.out.println(line);
		} while (line != null);
	}

	@Override
	@Trace(operationName = "run", resourceName = "NetIO")
	public void run() {

		Context.getInstance().recordTraceRoot(11111111111L, "/netio", 1024);
		Context.getInstance().setContext(22222222222L, 11111111111L);

		try {
			for (int i = 0; i < 10; i++) {
				sendRequest();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}


class SyncWait implements Runnable {

	static final Object lock = new Object();

	@Trace(operationName = "runNotify", resourceName = "SyncWait")
	@Override
	public void run() {
		for (int i = 0; i < 50; i++) {
			try {
				syncWait(i);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Trace(operationName = "notify", resourceName = "SyncWait")
	public static void syncWait(int i) throws InterruptedException {
		synchronized (lock) {
			lock.wait(new Random().nextInt(150));
			Fibonacci.fibonacci(30);
			System.out.println("thread wait timeout: " + i);
		}
	}
}


public class Server {
	private static final Gson GSON;
	private static volatile List<Movie> CACHED_MOVIES;

	static {
		GSON = new GsonBuilder().setLenient().create();
	}

	public static void main(String[] args) {
		port(8080);
		get("/movies", Server::moviesEndpoint);

		exception(Exception.class, (exception, request, response) -> {
			System.err.println(exception.getMessage());
			exception.printStackTrace();
		});
	}

	@Trace(operationName = "handleRequest", resourceName = "/movies")
	private static Object moviesEndpoint(Request req, Response res) throws InterruptedException, IOException {

		Context.getInstance().recordTraceRoot(333333333333L, "/movies", 1024);
		Context.getInstance().setContext(4444444444444L, 333333333333L);

		Thread thread1 = new Thread(NetIO.getInstance());
		thread1.start();


		new SyncWait().run();

//		Thread thread2 = new Thread(new FileIO());
//		thread2.start();

//		Thread thread3 = new Thread(new NetIO());
//		thread3.start();

		NetIO.getInstance().run();

		Fibonacci.getInstance().run();

		Stream<Server.Movie> movies = getMovies().stream();
		movies = sortByDescReleaseDate(movies);
		String query = req.queryParamOrDefault("q", req.queryParams("query"));
		if (query != null) {
			// Problem: We are not compiling the pattern and there's a more efficient way of ignoring cases.
			// Solution:
			//   Pattern p = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
			//   movies = movies.filter(m -> p.matcher(m.title).find());
			movies = movies.filter(m -> Pattern.matches(".*" + query.toUpperCase() + ".*", m.title.toUpperCase()));
		}
//		thread2.join();
//		thread3.join();
//		thread4.join();
		Object object = replyJSON(res, movies);
		thread1.join();
		return object;
	}

	@Trace(operationName = "sortByDescReleaseDate", resourceName = "/movies")
	private static Stream<Movie> sortByDescReleaseDate(Stream<Movie> movies) {
		return movies.sorted(Comparator.comparing((Movie m) -> {
			// Problem: We are parsing a datetime for each item to be sorted.
			// Example Solution:
			//   Since date is in isoformat (yyyy-mm-dd) already, that one sorts nicely with normal string sorting
			//   `return m.releaseDate`
			try {
				return LocalDate.parse(m.releaseDate);
			} catch (Exception e) {
				return LocalDate.MIN;
			}
		}).reversed());
	}

	@Trace(operationName = "replyJSONStream", resourceName = "/movies")
	private static Object replyJSON(Response res, Stream<?> data) {
		return replyJSON(res, data.collect(Collectors.toList()));
	}

	@Trace(operationName = "replyJSONObject", resourceName = "/movies")
	private static Object replyJSON(Response res, Object data) {
		res.type("application/json");
		return GSON.toJson(data);
	}

	@Trace(operationName = "getMovies", resourceName = "/movies")
	private static List<Movie> getMovies() {
		if (CACHED_MOVIES != null) {
			return CACHED_MOVIES;
		}

		return loadMovies();
	}

	@Trace(operationName = "loadMovies", resourceName = "/movies")
	private synchronized static List<Movie> loadMovies() {
		if (CACHED_MOVIES != null) {
			return CACHED_MOVIES;
		}

		try (
				InputStream is = ClassLoader.getSystemResourceAsStream("movies5000.json.gz");
				GZIPInputStream gzis = new GZIPInputStream(is);
				InputStreamReader reader = new InputStreamReader(gzis)
		){
			return CACHED_MOVIES = GSON.fromJson(reader, new TypeToken<List<Movie>>() {}.getType());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to load movie data");
		}
	}

	public static class Movie {
		public String title;
		@SerializedName("vote_average")
		public double rating;
		@SerializedName("release_date")
		public String releaseDate;
	}
}

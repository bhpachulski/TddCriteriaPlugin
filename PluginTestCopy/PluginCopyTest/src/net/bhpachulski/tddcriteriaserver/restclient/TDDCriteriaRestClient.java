package net.bhpachulski.tddcriteriaserver.restclient;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import net.bhpachulski.tddcriteriaserver.model.FileType;
import net.bhpachulski.tddcriteriaserver.model.Student;
import net.bhpachulski.tddcriteriaserver.model.StudentFile;
import net.bhpachulski.tddcriteriaserver.restclient.url.CriteriaTddGenericUrl;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.MultipartContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Maps;
import com.google.gson.Gson;

public class TDDCriteriaRestClient {

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final GsonFactory JSON_FACTORY = new GsonFactory();
	private Gson gson = new Gson();

	HttpRequestFactory requestFactory = HTTP_TRANSPORT
			.createRequestFactory(new HttpRequestInitializer() {
				@Override
				public void initialize(HttpRequest request) throws IOException {
					request.setParser(new JsonObjectParser(JSON_FACTORY));
				}
			});

	public String sendStudentFile(Integer studentId, File f) {
		try {

			MultipartContent content = new MultipartContent()
					.setMediaType(new HttpMediaType("multipart/form-data")
							.setParameter("boundary", "__END_OF_PART__"));

			FileContent fileContent = new FileContent("application/xml", f);
			MultipartContent.Part partFile = new MultipartContent.Part(
					new HttpHeaders().set("Content-Disposition",
							String.format("form-data; name=\"%s\"", "file")),
					fileContent);
			content.addPart(partFile);

			Map<String, String> parameters = Maps.newHashMap();
			parameters.put("studentId", studentId.toString());
			parameters.put("fileType", FileType.JUNIT.getId().toString()); 

			for (String name : parameters.keySet()) {
				MultipartContent.Part part = new MultipartContent.Part(
						new ByteArrayContent(null, parameters.get(name).toString().getBytes()));
				part.setHeaders(new HttpHeaders().set("Content-Disposition",
						String.format("form-data; name=\"%s\"", name)));

				content.addPart(part);
			}

			HttpRequest request = requestFactory
					.buildPostRequest(
							new CriteriaTddGenericUrl(
									"http://localhost:10080/tddCriteria/service/tddCriteriaService/addStudentFile"),
							content);

			HttpResponse resp = request.execute();

			return f.getName();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("ECLIPSE ERROR ?");
		}
	}

	public Student createStudent(Student student) {
		try {
			HttpRequest request = requestFactory
					.buildPostRequest(
							new CriteriaTddGenericUrl(
									"http://localhost:10080/tddCriteria/service/tddCriteriaService/addStudent"),
							ByteArrayContent.fromString(null,
									gson.toJson(student)));

			request.getHeaders().setContentType("application/json");
			HttpResponse resp = request.execute();

			return (Student) gson.fromJson(resp.parseAsString(), Student.class);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("ECLIPSE ERROR ?");
		}
	}
}

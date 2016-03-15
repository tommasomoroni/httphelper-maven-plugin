package it.moroni.httphelper;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "http")
public class HttpHelperMojo extends AbstractMojo {

	@Parameter(required = true)
	private String filename;

	@Parameter(required = true)
	private String username;

	@Parameter(required = true)
	private String password;

	@Parameter(required = true)
	private String portalBaseUrl;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {

		try {
			Credentials credentials = new UsernamePasswordCredentials(username, password);
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY, credentials);
			CloseableHttpClient httpclient =
					HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();

			HttpPost httpPost = new HttpPost(portalBaseUrl + "/server-manager-web/plugins");

			HttpEntity entity =
					MultipartEntityBuilder.create().addPart("file", new FileBody(new File(filename))).build();

			httpPost.setEntity(entity);
			CloseableHttpResponse response = httpclient.execute(httpPost);
			response.close();
			getLog().info("Post completed");
			httpclient.close();
		} catch (IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}

	}
}

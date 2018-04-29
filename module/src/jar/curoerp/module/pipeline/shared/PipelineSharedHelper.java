package jar.curoerp.module.pipeline.shared;

import java.io.IOException;
import java.security.MessageDigest;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.*;

public class PipelineSharedHelper {

	public static String sha256(String base) {
		try{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(base.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		} catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}

	public static String serialize(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public static <T> T deserialize(Class<T> cls, String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, cls);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public static String parseStatement(PipelineTag tag, String content) {
		StringBuilder stmt = new StringBuilder();
		String tagInfo = ";" + tag.getId() + ";" + tag.getType().toString()  + ";" + tag.getHash() + ";" + tag.getCls().getName() + ";";
		stmt.append("#START" + tagInfo + "\n");
		stmt.append(content + "\n");
		stmt.append("#END" + tagInfo + "\n");
		return stmt.toString();
	}

}

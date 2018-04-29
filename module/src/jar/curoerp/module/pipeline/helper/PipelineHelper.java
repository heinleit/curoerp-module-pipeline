package jar.curoerp.module.pipeline.helper;

import java.io.IOException;
import java.security.MessageDigest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PipelineHelper {
	public static PipelineTag parseTag(String tag, String line) {
		tag = tag.trim();
		line = line.trim();
		if(line.matches("#" + tag.toUpperCase() + ";[0-9]*;[a-zA-Z0-9]*;[a-fA-F0-9]{64};[a-zA-Z0-9_.]*;")) {
			String[] parts = line.split(";");
			int id = Integer.parseInt(parts[1]);
			PipelineType type = PipelineType.valueOf(parts[2]);
			String hash = parts[3];
			String cls = parts[4];
			try {
				return new PipelineTag(id, type, hash, Class.forName(cls));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
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

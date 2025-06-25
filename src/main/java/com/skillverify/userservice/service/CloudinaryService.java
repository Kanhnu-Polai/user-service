package com.skillverify.userservice.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
	
	private final Cloudinary cloudinary;

	public String uploadFile(MultipartFile file, String folder) throws IOException {
		String resourceType = detectResourceType(file);

		Map uploadResult = cloudinary.uploader().upload(
			file.getBytes(),
			ObjectUtils.asMap(
				"folder", folder,
				"resource_type", "raw",
				"access_mode", "public"// 👈 required for non-image files
			)
		);
		return (String) uploadResult.get("secure_url");
	}

	private String detectResourceType(MultipartFile file) {
		String contentType = file.getContentType();
		if (contentType != null && contentType.equals("application/pdf")) {
			return "raw"; // 📄 PDFs must be uploaded as raw
		}
		return "auto"; // auto-detect other types (like image/jpeg)
	}
}
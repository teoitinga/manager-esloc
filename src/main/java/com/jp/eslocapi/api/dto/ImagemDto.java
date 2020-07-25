package com.jp.eslocapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImagemDto {

	private Long id;
	private String fileName;
	private String mimeType;
	private String base64;
	
}

package com.attendance.system.member.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
public class QrCodeController {

	private static final int QR_CODE_SIZE = 320;

	@GetMapping(value = "/member/qr", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> memberQrCode(HttpServletRequest request) throws WriterException, IOException {
		String attendanceEntryUrl = ServletUriComponentsBuilder.fromRequestUri(request)
				.replacePath("/member/attendance/new")
				.replaceQuery(null)
				.build()
				.toUriString();

		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix matrix = qrCodeWriter.encode(
				attendanceEntryUrl,
				BarcodeFormat.QR_CODE,
				QR_CODE_SIZE,
				QR_CODE_SIZE,
				Map.of(
						EncodeHintType.CHARACTER_SET, "UTF-8",
						EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M
				)
		);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);

		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_PNG)
				.body(outputStream.toByteArray());
	}
}

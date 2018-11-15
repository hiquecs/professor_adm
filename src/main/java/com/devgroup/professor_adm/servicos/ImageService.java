package com.devgroup.professor_adm.servicos;

import java.awt.image.BufferedImage;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

	public BufferedImage cropSquare(BufferedImage sourceImg) {

		int min = (sourceImg.getHeight() <= sourceImg.getWidth()) ? sourceImg.getHeight() : sourceImg.getWidth();
		return Scalr.crop(sourceImg, (sourceImg.getWidth() / 2) - (min / 2), (sourceImg.getHeight() / 2) - (min / 2),
				min, min);
	}

	public BufferedImage resize(BufferedImage sourceImg, int size) {
		return Scalr.resize(sourceImg, Scalr.Method.ULTRA_QUALITY, size);
	}
}
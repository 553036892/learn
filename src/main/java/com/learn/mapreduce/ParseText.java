package com.learn.mapreduce;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class ParseText {
	public static HashSet<String> types;
	static {
		types = new HashSet<>();
		types.add("doc");
		types.add("docx");
		types.add("xls");
		types.add("xlsx");
		types.add("ppt");
		types.add("pptx");
		types.add("pdf");
		types.add("txt");
	}

	// 判断文档类型，调用不同的解析方法
	public static String parse(InputStream byteArrayInputStream, String suffix) {
		String text = "";
		switch (suffix) {
			case "doc":
				text = getTextFromWord(byteArrayInputStream);
				break;
			case "docx":
				text = getTextFromWord2007(byteArrayInputStream);
				break;
			case "xls":
				text = getTextFromExcel(byteArrayInputStream);
				break;
			case "xlsx":
				text = getTextFromExcel2007(byteArrayInputStream);
				break;
			case "ppt":
				text = getTextFromPPT(byteArrayInputStream);
				break;
			case "pptx":
				text = getTextFromPPT2007(byteArrayInputStream);
				break;
			case "pdf":
				text = getTextFormPDF(byteArrayInputStream);
				break;
			case "txt":
				text = getTextFormTxt(byteArrayInputStream);
				break;
			default:
				System.out.println("不支持解析的文档类型");
		}

		return text.replaceAll("\\s*", "");
	}

	public static boolean hastType(String type) {
		return types.contains(type);
	}

	// 读取Word97-2003的全部内容 doc
	private static String getTextFromWord(InputStream stm) {
		String text = "";
		InputStream fis = null;
		WordExtractor ex = null;
		try {
			// word 2003： 图片不会被读取
			ex = new WordExtractor(fis);
			text = ex.getText();
			ex.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}

	// 读取Word2007+的全部内容 docx
	private static String getTextFromWord2007(InputStream stm) {
		String text = "";
		XWPFDocument doc = null;
		XWPFWordExtractor workbook = null;
		try {
			doc = new XWPFDocument(stm);
			workbook = new XWPFWordExtractor(doc);
			text = workbook.getText();
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}

	// 读取Excel97-2003的全部内容 xls
	private static String getTextFromExcel(InputStream stm) {
		HSSFWorkbook wb = null;
		String text = "";
		try {
			wb = new HSSFWorkbook(new POIFSFileSystem(stm));
			ExcelExtractor extractor = new ExcelExtractor(wb);
			extractor.setFormulasNotResults(false);
			extractor.setIncludeSheetNames(false);
			text = extractor.getText();
			extractor.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}

	// 读取Excel2007+的全部内容 xlsx
	private static String getTextFromExcel2007(InputStream stm) {
		XSSFWorkbook workBook = null;
		String text = "";
		try {
			workBook = new XSSFWorkbook(stm);
			XSSFExcelExtractor extractor = new XSSFExcelExtractor(workBook);
			extractor.setIncludeSheetNames(false);
			text = extractor.getText();
			extractor.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}

	// 读取Powerpoint97-2003的全部内容 ppt
	private static String getTextFromPPT(InputStream stm) {
		String text = "";
		PowerPointExtractor ex = null;
		try {
			// word 2003： 图片不会被读取
			ex = new PowerPointExtractor(stm);
			text = ex.getText();
			ex.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}

	// 抽取幻灯片2007+全部内容 pptx
	private static String getTextFromPPT2007(InputStream stm) {
		XMLSlideShow slide = null;
		String text = "";
		try {
			slide = new XMLSlideShow(stm);
			XSLFPowerPointExtractor extractor = new XSLFPowerPointExtractor(slide);
			text = extractor.getText();
			extractor.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}

	// 读取pdf文件全部内容 pdf
	private static String getTextFormPDF(InputStream stm) {
		String text = "";
		PDDocument pdfdoc = null;
		try {
			pdfdoc = PDDocument.load(stm);
			PDFTextStripper stripper = new PDFTextStripper();
			text = stripper.getText(pdfdoc);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pdfdoc != null) {
					pdfdoc.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return text;
	}

	// 读取txt文件全部内容 txt
	private static String getTextFormTxt(InputStream stm) {
		String text = "";
		try {
			ByteArrayOutputStream ostm = new ByteArrayOutputStream();
			byte[] buf = new byte[1];
			while (stm.read(buf) > 0) {
				ostm.write(buf);
			}
			text = new String(ostm.toByteArray());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return text;
	}

	// 获得txt文件编码方式
	private static String get_charset(InputStream stm) throws IOException {
		String charset = "GBK";
		byte[] first3Bytes = new byte[3];
		try {
			boolean checked = false;
			stm.mark(0);
			int read = stm.read(first3Bytes, 0, 3);
			if (read == -1)
				return charset;
			if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8";
				checked = true;
			}
			stm.reset();
			if (!checked) {
				while ((read = stm.read()) != -1) {
					if (read >= 0xF0)
						break;
					if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
						break;
					if (0xC0 <= read && read <= 0xDF) {
						read = stm.read();
						if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
							// (0x80 - 0xBF),也可能在GB编码内
							continue;
						else
							break;
					} else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
						read = stm.read();
						if (0x80 <= read && read <= 0xBF) {
							read = stm.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else
								break;
						} else
							break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stm != null) {
				stm.close();
			}
		}
		return charset;
	}
//			public static void main(String[] args) throws IOException {
//				File s = new File("C:\\Users\\esensoft\\Desktop\\word.docx");
//				String text = "";
//				XWPFDocument doc = null;
//				XWPFWordExtractor workbook = null;
//				InputStream resourceAsStream = new FileInputStream(s);
//				doc = new XWPFDocument(resourceAsStream);
//				workbook = new XWPFWordExtractor(doc);
//				text = workbook.getText();
//				workbook.close();
//				resourceAsStream.close();
//				
//				System.out.println(text);
//			}

}
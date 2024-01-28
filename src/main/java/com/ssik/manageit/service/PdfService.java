
package com.ssik.manageit.service;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ssik.manageit.domain.Question;
import com.ssik.manageit.domain.QuestionPaper;
import com.ssik.manageit.domain.QuestionType;

@Service
@Transactional
public class PdfService {
	private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
	private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

	public List<String> readFromPdf(byte[] bytes) {
		List<String> lines = new ArrayList<String>();

		try (PDDocument document = PDDocument.load(new ByteArrayInputStream(bytes))) {

			document.getClass();

			if (!document.isEncrypted()) {

				PDFTextStripperByArea stripper = new PDFTextStripperByArea();
				stripper.setSortByPosition(true);

				PDFTextStripper tStripper = new PDFTextStripper();

				String pdfFileInText = tStripper.getText(document);
				// System.out.println("Text:" + st);

				// split by whitespace
				String linesFromPdf[] = pdfFileInText.split("\\r?\\n");
				for (String line : linesFromPdf) {
					System.out.println(line);
					lines.add(line);
				}

			}

		} catch (InvalidPasswordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;
	}

	private Paragraph addContentToParagraph(int align, Font font, String text) {
		Paragraph para = new Paragraph(text, font);
		para.setAlignment(align);
		return para;
	}

	private PdfPCell getCell(String text, Font font, int align) {
		PdfPCell cell = new PdfPCell(new Phrase("Some text here", font));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(align);
		return cell;
	}

	public void writeToPdf(QuestionPaper questionPaper) {
		Set<Question> questions = questionPaper.getQuestions();
		Map<QuestionType, List<Question>> questionsOfEachType = questions.stream()
				.collect(Collectors.groupingBy(q -> q.getQuestionType()));

		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(
					"/tmp/" + questionPaper.getId() + "_" + questionPaper.getQuestionPaperName() + ".pdf"));

			document.open();

			Paragraph headerPara = new Paragraph();
			// We add one empty line
			addEmptyLine(headerPara, 1);
			// Lets write a big header
			headerPara.add(addContentToParagraph(Element.ALIGN_CENTER, catFont, questionPaper.getMainTitle()));
			headerPara.add(addContentToParagraph(Element.ALIGN_CENTER, subFont, questionPaper.getSubTitle()));
			document.add(headerPara);

			PdfPTable table = new PdfPTable(2);
			table.addCell(
					getCell("Class : " + questionPaper.getSchoolClass().getClassName(), smallBold, Element.ALIGN_LEFT));
			table.addCell(getCell("Subject : " + questionPaper.getClassSubject().getSubjectName(), smallBold,
					Element.ALIGN_RIGHT));
			table.addCell(getCell("Total Duration : ", smallBold, Element.ALIGN_LEFT));
			table.addCell(getCell("Total marks : ", smallBold, Element.ALIGN_RIGHT));

			document.add(table);

			com.itextpdf.text.List orderedList = new com.itextpdf.text.List(com.itextpdf.text.List.ORDERED, 10);
			for (QuestionType questionType : questionsOfEachType.keySet()) {
				orderedList.add(new ListItem(
						questionType.getQuestionType() + " ( Each carries " + questionType.getMarks() + "mark"));
				// Print header for the question type
				// get all Questions
				List<Question> questionOfThisType = questionsOfEachType.get(questionType);
				com.itextpdf.text.RomanList subOrderedList = new com.itextpdf.text.RomanList(
						com.itextpdf.text.RomanList.ORDERED, 20);
				for (Question question : questionOfThisType) {
					// print each question
					// Print question text
					// if there is an image, print the image
					subOrderedList.add(new ListItem(question.getQuestionText()));

					if (question.getQuestionImage() != null) {
//						String path = "/tmp/" + questionPaper.getId() + "_" + questionPaper.getQuestionPaperName()
//								+ "_ques_" + question.getId() + ".jpg";
//						File tmpOutfile = new File(path);
//						Files.write(tmpOutfile.toPath(), question.getQuestionImage());

						// Creating an Image object
						Image image = Image.getInstance(question.getQuestionImage());

						// Adding image to the document
						document.add(image);
					}
					// if there is an answer image, then print that as well
					if (question.getAnswerImage() != null) {
						Image image = Image.getInstance(question.getAnswerImage());

						// Adding image to the document
						document.add(image);

					}

					// if there are options, print the options -print it in tables

					// this is last one -- if there are options and they are separated by colon, it
					// means this is a match the following -print the table
					com.itextpdf.text.RomanList optionsList = new com.itextpdf.text.RomanList(
							com.itextpdf.text.RomanList.ORDERED, 40);
					if (question.getOption1() != null && question.getOption2() != null && question.getOption3() != null
							&& question.getOption4() != null) {
						optionsList.add(new ListItem(question.getOption1()));
						optionsList.add(new ListItem(question.getOption2()));
						optionsList.add(new ListItem(question.getOption3()));
						optionsList.add(new ListItem(question.getOption4()));
						subOrderedList.add(optionsList);
					}
				}
				orderedList.add(subOrderedList);
			}
			document.add(orderedList);
			document.close();

		} catch (FileNotFoundException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	public static void main(String[] args) throws IOException {

//		try (PDDocument document = PDDocument.load(
//				new File("/Users/chandan/projects/cpandey05/java/self/ssik/src/main/resources/sample-question.pdf"))) {
//
//			document.getClass();
//
//			if (!document.isEncrypted()) {
//
//				PDFTextStripperByArea stripper = new PDFTextStripperByArea();
//				stripper.setSortByPosition(true);
//
//				PDFTextStripper tStripper = new PDFTextStripper();
//
//				String pdfFileInText = tStripper.getText(document);
//				// System.out.println("Text:" + st);
//
//				// split by whitespace
//				String lines[] = pdfFileInText.split("\\r?\\n");
//				for (String line : lines) {
//					System.out.println(line);
//				}
//
//			}
//
//		}

		Document document = new Document();

		try {
			PdfWriter.getInstance(document, new FileOutputStream("/tmp/list.pdf"));

			document.open();

			com.itextpdf.text.List orderedList = new com.itextpdf.text.List(com.itextpdf.text.List.ORDERED, 10);
			orderedList.add(new ListItem("Item 1"));
			orderedList.add(new ListItem("Item 2"));
			orderedList.add(new ListItem("Item 3"));
			com.itextpdf.text.RomanList subOrderedList = new com.itextpdf.text.RomanList(
					com.itextpdf.text.RomanList.ORDERED, 20);
			subOrderedList.add(new ListItem("Sub Item 1"));
			subOrderedList.add(new ListItem("Sub Item 2"));
			orderedList.add(subOrderedList);

			document.add(orderedList);

			com.itextpdf.text.List unorderedList = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
			unorderedList.add(new ListItem("Item 1"));
			unorderedList.add(new ListItem("Item 2"));
			unorderedList.add(new ListItem("Item 3"));

			document.add(unorderedList);

			document.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (com.itextpdf.text.DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

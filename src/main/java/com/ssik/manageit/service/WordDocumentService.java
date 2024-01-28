package com.ssik.manageit.service;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFAbstractNum;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFNumbering;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssik.manageit.domain.Question;
import com.ssik.manageit.domain.QuestionPaper;
import com.ssik.manageit.domain.QuestionType;

@Service
@Transactional
public class WordDocumentService {

	public List<String> processWordDocument(byte[] bytes) {
		List<String> questions = new ArrayList<String>();

		XWPFWordExtractor extractor = null;
		try {
			InputStream bis = new ByteArrayInputStream(bytes);
			XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(bis));
			extractor = new XWPFWordExtractor(xdoc);
			// System.out.println(extractor.getText());

			// System.out.println(" ====================== PARA TEST
			// ===========================");
			// output the same as 8.1
			List<XWPFParagraph> list = xdoc.getParagraphs();
			for (XWPFParagraph paragraph : list) {
				questions.add(paragraph.getText());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (extractor != null) {
				try {
					extractor.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return questions;
	}

	public void dumpQuestionPaperToWordDoc(QuestionPaper questionPaper) {
		if (questionPaper.getQuestions() == null) {
			// return null;
			return;
		}

		Set<Question> questions = questionPaper.getQuestions();
		Map<QuestionType, List<Question>> questionsOfEachType = questions.stream()
				.collect(Collectors.groupingBy(q -> q.getQuestionType()));

		try (XWPFDocument doc = new XWPFDocument()) {
			// Create numbering
			// CTAbstractNum cTAbstractNum = CTAbstractNum.Factory.newInstance();
			// Next we set the AbstractNumId. This requires care.
			// Since we are in a new document we can start numbering from 0.
			// But if we have an existing document, we must determine the next free
			// number first.
			// cTAbstractNum.setAbstractNumId(BigInteger.valueOf(0));

			/*
			 * Bullet list CTLvl cTLvl = cTAbstractNum.addNewLvl();
			 * cTLvl.setIlvl(BigInteger.valueOf(0)); // set indent level 0
			 * cTLvl.addNewNumFmt().setVal(STNumberFormat.BULLET);
			 * cTLvl.addNewLvlText().setVal("•");
			 */

//			CTLvl cTLvl = cTAbstractNum.addNewLvl();
//			cTLvl.setIlvl(BigInteger.valueOf(0)); // set indent level 0
//			cTLvl.addNewNumFmt().setVal(STNumberFormat.DECIMAL);
//			cTLvl.addNewLvlText().setVal("%1.");
//			cTLvl.addNewStart().setVal(BigInteger.valueOf(1));
//
//			XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
//			XWPFNumbering numbering = doc.createNumbering();
//
//			BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);
//			BigInteger numID = numbering.addNum(abstractNumID);

			// save the docs
//            try (FileOutputStream out = new FileOutputStream("c:\\test\\simple.docx")) {
//                doc.write(out);
//            }
//			XWPFTable table = doc.createTable();
//			table.setWidth("100%");
//			// create first row
//			XWPFTableRow tableRowOne = table.getRow(0);
////			XWPFTableCell cell0 = tableRowOne.getCell(0);
////			cell0.addParagraph();
////			XWPFParagraph p1 = cell0.getParagraphs().get(0);
////			// p1.setAlignment(ParagraphAlignment.LEFT);
////			XWPFRun r1 = p1.createRun();
////			r1.setText("CUSTOMER_NAME".trim());
//			tableRowOne.getCell(0).getParagraphs().get(0).setAlignment(ParagraphAlignment.LEFT);
//			tableRowOne.addNewTableCell().setText("col two, row one");
//			tableRowOne.getCell(1).getParagraphs().get(0).setAlignment(ParagraphAlignment.RIGHT);

			addParagraph(doc, ParagraphAlignment.CENTER, true, false, 35, questionPaper.getMainTitle(), "Courier");
			addParagraph(doc, ParagraphAlignment.CENTER, true, false, 26, questionPaper.getSubTitle(), "Courier");
			addNewLine(doc);
			addParagraph(doc, ParagraphAlignment.LEFT, true, false, 35,
					questionPaper.getClassSubject().getSubjectName(), "Courier");
			addParagraph(doc, ParagraphAlignment.LEFT, true, false, 35,
					"Total Marks : " + questionPaper.getTotalMarks(), "Courier");

			XWPFParagraph p1 = doc.createParagraph();
			p1.setAlignment(ParagraphAlignment.LEFT);
			// p1.setNumID(numID);
			// Set Text to Bold and font size to 22 for first paragraph
			XWPFRun r1 = p1.createRun();

//			r1.setBold(bold);
//			r1.setItalic(italic);
//			r1.setFontSize(fontSize);
			r1.setText("Chandan \t \t  tab Left");
			r1.addTab();
			r1.addTab();
			r1.addTab();
			r1.addTab();
			r1.setText("Chandan test Right");
			// r1.setFontFamily(fontFamily);

			// create table
			// tableRowOne.addNewTableCell().setText("col three, row one");

			// create second row
//			XWPFTableRow tableRowTwo = table.createRow();
//			tableRowTwo.getCell(0).setText("col one, row two");
//			tableRowTwo.getCell(1).setText("col two, row two");
//			tableRowTwo.getCell(2).setText("col three, row two");

			int questionTypeNumber = 1;
			for (QuestionType questionType : questionsOfEachType.keySet()) {
				int questionNumber = 1;
				// Print header for the question type
				addParagraph(
						doc, ParagraphAlignment.LEFT, true, false, 17, "    " + questionTypeNumber++ + "    "
								+ questionType.getQuestionType() + "(" + questionType.getMarks() + " mark each)",
						"Courier");

				// get all Questions
				List<Question> questionOfThisType = questionsOfEachType.get(questionType);
				for (Question question : questionOfThisType) {
					// print each question
					// Print question text
					addParagraph(doc, ParagraphAlignment.LEFT, true, false, 12, "        " + questionTypeNumber + "."
							+ questionNumber + "    " + question.getQuestionText(), "Courier");
					questionNumber++;
					// if there is an image, print the image
					if (question.getQuestionImage() != null) {

					}
					// if there is an answer image, then print that as well
					if (question.getAnswerImage() != null) {

					}

					// if there are options, print the options -print it in tables

					// this is last one -- if there are options and they are separated by colon, it
					// means this is a match the following -print the table
				}

			}
			try (FileOutputStream out = new FileOutputStream("/tmp/simple1.docx")) {
				doc.write(out);
//           }
//			try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//				doc.write(out);
				// return out.toByteArray();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// return null;
	}
//	 XWPFRun r1 = p1.createRun();
//     r1.setBold(true);
//     r1.setItalic(true);
//     r1.setFontSize(22);
//     r1.setText("I am first paragraph. My Text is bold, italic, Courier and capitalized");
//     r1.setFontFamily("Courier");

	private void addParagraph(XWPFDocument doc, ParagraphAlignment paragraphAlignment, boolean bold, boolean italic,
			int fontSize, String text, String fontFamily) {
		XWPFParagraph p1 = doc.createParagraph();
		p1.setAlignment(paragraphAlignment);
		// p1.setNumID(numID);
		// Set Text to Bold and font size to 22 for first paragraph
		XWPFRun r1 = p1.createRun();
		r1.setBold(bold);
		r1.setItalic(italic);
		r1.setFontSize(fontSize);
		r1.setText(text);
		r1.setFontFamily(fontFamily);

	}

	private void addNewLine(XWPFDocument doc) {
		XWPFParagraph p1 = doc.createParagraph();

		XWPFRun r1 = p1.createRun();
		r1.addCarriageReturn();

	}

	public static void main(String[] args) {
		XWPFDocument document = new XWPFDocument();

		XWPFParagraph paragraph = document.createParagraph();
		XWPFRun run = paragraph.createRun();
		run.setText("The list:");

		ArrayList<String> documentList = new ArrayList<String>(Arrays.asList(new String[] { "One", "Two", "Three" }));

		CTAbstractNum cTAbstractNum = CTAbstractNum.Factory.newInstance();
		// Next we set the AbstractNumId. This requires care.
		// Since we are in a new document we can start numbering from 0.
		// But if we have an existing document, we must determine the next free number
		// first.
		cTAbstractNum.setAbstractNumId(BigInteger.valueOf(0));

		/*
		 * Bullet list CTLvl cTLvl = cTAbstractNum.addNewLvl();
		 * cTLvl.setIlvl(BigInteger.valueOf(0)); // set indent level 0
		 * cTLvl.addNewNumFmt().setVal(STNumberFormat.BULLET);
		 * cTLvl.addNewLvlText().setVal("•");
		 */

		/// * Decimal list
		CTLvl cTLvl = cTAbstractNum.addNewLvl();
		cTLvl.setIlvl(BigInteger.valueOf(0)); // set indent level 0
		cTLvl.addNewNumFmt().setVal(STNumberFormat.DECIMAL);
		cTLvl.addNewLvlText().setVal("%1.");
		cTLvl.addNewStart().setVal(BigInteger.valueOf(1));
		// */

		XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);

		XWPFNumbering numbering = document.createNumbering();

		BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);

		BigInteger numID = numbering.addNum(abstractNumID);

		for (String string : documentList) {
			paragraph = document.createParagraph();
			paragraph.setNumID(numID);
			run = paragraph.createRun();
			run.setText(string);
		}

		paragraph = document.createParagraph();

		FileOutputStream out;
		try {
			out = new FileOutputStream("/tmp/CreateWordSimplestNumberingList.docx");
			document.write(out);
			out.close();
			document.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

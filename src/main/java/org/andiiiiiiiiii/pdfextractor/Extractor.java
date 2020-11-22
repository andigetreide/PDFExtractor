package org.andiiiiiiiiii.pdfextractor;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Extractor {

    private static final String OUTPUT_DIR = "tmp/";
    private static final String INPUTFILE = "vo1.pdf";

    public static void extract(String[] args) throws Exception {

        PDFTextStripper pdfStripper = new PDFTextStripper();

        try (final PDDocument document = PDDocument.load(new File(INPUTFILE))) {

            String filename;

            PDPageTree list = document.getPages();
            int countPics=0;
            int countPages = 0;
            for (PDPage page : list) {
                countPages++;
                //System.out.println("Page: " + countPages);

                pdfStripper.setStartPage(countPages);
                pdfStripper.setEndPage(countPages);
                String parsedText = pdfStripper.getText(document);

                filename = String.format(OUTPUT_DIR + "page%03d.txt", countPages);
                FileWriter textOutput = new FileWriter(filename);
                textOutput.write(parsedText);
                textOutput.close();

                PDResources pdResources = page.getResources();
                for (COSName name : pdResources.getXObjectNames()) {
                    PDXObject o = pdResources.getXObject(name);
                    if (o instanceof PDImageXObject) {
                        countPics++;
                        //System.out.println("Picture " + countPics);
                        PDImageXObject image = (PDImageXObject)o;
                        filename = String.format(OUTPUT_DIR + "page%03d-pic%03d.png", countPages, countPics);

                        ImageIO.write(image.getImage(), "png", new File(filename));
                    }
                }
            }
            pdfStripper.setStartPage(1);
            pdfStripper.setEndPage(document.getNumberOfPages());
            String parsedText = pdfStripper.getText(document);

            FileWriter textOutput = new FileWriter(OUTPUT_DIR + "text.txt");
            textOutput.write(parsedText);
            textOutput.close();

        } catch (IOException e){
            System.err.println("Exception while trying to create pdf document - " + e);
        }
    }

}

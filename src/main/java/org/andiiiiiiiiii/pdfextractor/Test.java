package org.andiiiiiiiiii.pdfextractor;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;

// https://issues.apache.org/jira/browse/PDFBOX-5036 created
// let's see what happens
// using the twelvemonkeys library solves the problem! :-)

public class Test {
    public static void main(String[] args) {

        try {
            PDDocument document = PDDocument.load(new File("Phaeton.pdf"));
            PDResources pdRes = document.getPages().get(0).getResources();
            for (COSName name : pdRes.getXObjectNames()) {
                PDXObject o = pdRes.getXObject(name);
                if (o instanceof PDImageXObject) {
                    PDImageXObject img = (PDImageXObject) o;
                    img.getImage();  // This throws an java.lang.IllegalArgumentException (runtime exception) - bug in PDFBOX???
                }
            }
        } catch(IOException e) {
            System.out.println("IOException thrown...");
        }
    }
}

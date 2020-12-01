package org.andiiiiiiiiii.pdfextractor;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

/**
 * This class contains the logic for the PDF Extraction
 * it is independent of the user interface
 */
public class Extractor {

    private boolean overwriteFiles;
    private boolean exportImages;
    private boolean exportTextPerPage;
    private boolean exportSingleTextfile;
    private boolean addPageNumbers;

    private DoubleProperty progress = new SimpleDoubleProperty(0);

    /*public final double getStand() {
        if (stand != null)
            return stand.get();
        return 0;
    }*/

    /*public final void setStand(double hoehe) {
        this.standProperty().set(hoehe);
    }*/

    // Property handling taken from https://javabeginners.de/Frameworks/JavaFX/Properties_und_Binding.php
    public final DoubleProperty progressProperty() {
        /*if (progress == null) {
            progress = new SimpleDoubleProperty(0);
        }*/
        return progress;
    }


    public void setOverwriteFiles(boolean overwriteFiles) {
        this.overwriteFiles = overwriteFiles;
    }

    public void setExportImages(boolean exportImages) {
        this.exportImages = exportImages;
    }

    public void setExportTextPerPage(boolean exportTextPerPage) {
        this.exportTextPerPage = exportTextPerPage;
    }

    public void setExportSingleTextfile(boolean exportSingleTextfile) {
        this.exportSingleTextfile = exportSingleTextfile;
    }

    public void setAddPageNumbers(boolean addPageNumbers) {
        this.addPageNumbers = addPageNumbers;
    }

    public void extract(@NotNull String inputfile, @NotNull String outputdir) {
        extract(inputfile, outputdir, System.out);
    }

    public void extract(@NotNull String inputfile, @NotNull String outputdir, @NotNull PrintStream out) {
        extract(inputfile, outputdir, out, out);
    }

    /**
     * Extractor with no parameters sets default options
     */
    public Extractor() {
        this.overwriteFiles = false;
        this.exportImages = true;
        this.exportTextPerPage = true;
        this.exportSingleTextfile = true;
        this.addPageNumbers = true;
    }

    public Extractor(boolean overwriteFiles, boolean exportImages, boolean exportTextPerPage, boolean exportSingleTextfile, boolean addPageNumbers) {
        this.overwriteFiles = overwriteFiles;
        this.exportImages = exportImages;
        this.exportTextPerPage = exportTextPerPage;
        this.exportSingleTextfile = exportSingleTextfile;
        this.addPageNumbers = addPageNumbers;
    }

    /**
     *
     * @param inputfile PDF input file
     * @param outputdir output directory
     * @param out PrintStream to print output to (e.g. System.out)
     */
    public void extract(
            @NotNull String inputfile,
            @NotNull String outputdir,
            @NotNull PrintStream out,
            @NotNull PrintStream err) {

        String inFileNoExt = new File(inputfile).getName();
        int dot = inFileNoExt.lastIndexOf('.');
        inFileNoExt =  (dot == -1) ? inFileNoExt : inFileNoExt.substring(0, dot);

        if(inputfile.equals("")) {
            err.println("Please specify PDF File");
            return;
        }

        if (outputdir.equals("")) {
            err.println("Please specify output directory");
            return;
        }

        if (outputdir.charAt(outputdir.length()-1) != File.separatorChar)
            outputdir = outputdir + File.separator;

        if(!(new File(outputdir).exists())) {
            err.println("Output directory does not exist!");
            return;
        }

        // Don't know when this new PDFStripper() exception is really thrown
        // (API: "IOException - If there is an error loading the properties." But which properties??? )
        try {
            PDFTextStripper pdfStripper = new PDFTextStripper();

            try (final PDDocument document = PDDocument.load(new File(inputfile))) {

                String filename;

                PDPageTree list = document.getPages();
                int countPics=0;
                int countPages = 0;

                out.println("Starting PDF Extraction...");

                for (PDPage page : list) {
                    countPages++;

                    this.progressProperty().set((double) countPages / (double) document.getNumberOfPages());
                    // can the following be used instead?
                    // progress.set((double) countPages / (double) document.getNumberOfPages());

                    out.println("Scanning page " + countPages);

                    if (exportTextPerPage) {
                        pdfStripper.setStartPage(countPages);
                        pdfStripper.setEndPage(countPages);
                        String parsedText = pdfStripper.getText(document);

                        out.println("Exporting text from page " + countPages);
                        filename = String.format(outputdir + inFileNoExt + "_page%03d.txt", countPages);
                        if (!overwriteFiles) {
                            if (new File(filename).exists()) {
                                err.println("File " + filename + " already exists!");
                                return;
                            }
                        }
                        try {
                            FileWriter textOutput = new FileWriter(filename);
                            textOutput.write(parsedText);
                            textOutput.close();
                        } catch (IOException e) {
                            err.println("Cannot write file!");
                            return;
                        }
                    }

                    if (exportImages) {
                        PDResources pdResources = page.getResources();
                        for (COSName name : pdResources.getXObjectNames()) {
                            try {
                                // For some PDFs an exception is thrown here.... for whatever reason (e.g. for the System Safety Magazine)
                                PDXObject o = pdResources.getXObject(name);
                                if (o instanceof PDImageXObject) {
                                    countPics++;
                                    out.println("Exporting image " + countPics);
                                    PDImageXObject image = (PDImageXObject) o;
                                    filename = String.format(outputdir + inFileNoExt + "_page%03d-pic%03d.png", countPages, countPics);
                                    if (!overwriteFiles) {
                                        if (new File(filename).exists()) {
                                            err.println("File " + filename + " already exists!");
                                            return;
                                        }
                                    }
                                    try {
                                        ImageIO.write(image.getImage(), "png", new File(filename));
                                    } catch (IOException e) {
                                        err.println("File " + filename + " already exists!");
                                        return;
                                    }
                                }
                            }
                            catch (IOException e) {
                                err.println("Error while parsing PDF!");
                                return;
                            }
                        }
                    }
                }

                if(exportSingleTextfile) {
                    filename = outputdir + inFileNoExt + "_text.txt";
                    out.println("Writing complete text into one file.");
                    if (!overwriteFiles) {
                        if (new File(filename).exists()) {
                            err.println("File " + filename + " already exists!");
                            return;
                        }
                    }

                    try {
                        FileWriter textOutput = new FileWriter(filename);

                        for(int i = 1 ; i<=document.getNumberOfPages(); i++) {
                            if(addPageNumbers) {
                                textOutput.write("******* PDFExtractor - Page " + i + " *******\n");
                            }
                            pdfStripper.setStartPage(i);
                            pdfStripper.setEndPage(i);
                            String parsedText = pdfStripper.getText(document);
                            textOutput.write(parsedText);
                        }
                        textOutput.close();
                    } catch (IOException e) {
                        err.println("Cannot write to output file " + filename);
                        return;
                    }



                }

            } catch (IOException e) {
                err.println("Cannot open PDF document!");
                return;
            }

            out.println("Done!");
        } catch(IOException e) {
            err.println("Cannot create PDFTextStripper!");
        }


    }

}

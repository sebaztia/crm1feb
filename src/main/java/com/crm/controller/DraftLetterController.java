package com.crm.controller;

import com.crm.dto.LetterDto;
import com.crm.model.PersonalAsset;
import com.crm.service.AWSS3Service;
import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class DraftLetterController {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ClientController.class);
    private JavaMailSender mailSender;
    private AWSS3Service awss3Service;

    @Autowired
    public DraftLetterController(JavaMailSender mailSender, AWSS3Service awss3Service) {
        this.mailSender = mailSender;
        this.awss3Service = awss3Service;
    }

    @PostMapping("/draftWordFile")
    public ResponseEntity<InputStreamResource> draftWordFile(@ModelAttribute("draftLetter") LetterDto letterDto, BindingResult result, Model model, RedirectAttributes attributes) throws FileNotFoundException {
        File file = null;
        try {
          file = updateDocument(letterDto);
           // writeDoc(letterDto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                // Content-Type
                .header("Content-type", "application/octet-stream")
                // Contet-Length
                .contentLength(file.length()) //
                .body(resource);

        /*attributes.addAttribute("id", letterDto.getClientI());
        return "redirect:/showClient/{id}";*/
    }

    private File updateDocument(LetterDto letterDto) throws IOException, MessagingException {

        try (XWPFDocument document = new XWPFDocument(
                awss3Service.getTemplate("template.docx")
               // Files.newInputStream(Paths.get("src/template.docx"))
        )){
            List<XWPFParagraph> xwpfParagraphList = document.getParagraphs();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            String[] address = null;
            if (letterDto.getBankAddress() !=null) {
                address = letterDto.getBankAddress().split("\n");
            }
            for (XWPFParagraph xwpfParagraph : xwpfParagraphList) {
                for (XWPFRun xwpfRun : xwpfParagraph.getRuns()) {
                    String docText = xwpfRun.getText(0);
                    if (null != docText) {
                        System.out.println("docText===" + docText);
                        docText = docText.replace("nameCD", letterDto.getDeceasedName());
                        docText = docText.replace("addressCD", letterDto.getDeceasedAddress());
                        docText = docText.replace("dobCD", letterDto.getDoBirth());
                        docText = docText.replace("dodCD", letterDto.getDoDeath());
                        docText = docText.replace("maritalCD", letterDto.getMaritalStatus().equals("SELECT")? "" : letterDto.getMaritalStatus());
                        docText = docText.replace("dowCD", letterDto.getDoWill());

                        docText = docText.replace("exeName", letterDto.getExecutorName());
                        docText = docText.replace("exeAddress", letterDto.getExecutorAddress());
                        docText = docText.replace("relationship", letterDto.getRelationship());
                        docText = docText.replace("contactNumber", letterDto.getContactNumber());

                        if (docText.equals("bankAddressCD"))
                        for (int i=0; i < 5; i++) {
                            if (i < address.length) {
                                if (i==0) {
                                    docText = docText.replace("bankAddressCD", address[i]);
                                  //  xwpfRun.setText(docText, 0);
                                    xwpfRun.addBreak();
                                } else {
                                    xwpfRun.setText(address[i]);
                                    xwpfRun.addBreak();
                                }
                            } else {
                                xwpfRun.addBreak();
                            }
                        }
                    //    docText = docText.replace("bankAddressCD", letterDto.getBankAddress());
                        docText = docText.replace("ourRefCD", letterDto.getOurReference());
                        docText = docText.replace("yrRefCD", letterDto.getYourReference());
                        docText = docText.replace("currDate", sdf.format(new Date()));
                        docText = docText.replace("estateCD", letterDto.getEstateOf());
                        docText = docText.replace("accountNumberCD", letterDto.getAccountNumber());
                        docText = docText.replace("faithCD", getUsername());

                        xwpfRun.setText(docText, 0);
                    }
                }
            }

            File file = File.createTempFile("deceased details", ".docx");
            System.out.println("file getAbsolutePath===" + file.getAbsolutePath());
            try (FileOutputStream out = new FileOutputStream(/*new File("src/output.docx")*/ file)){
                document.write(out);
                //  sendEmail(letterDto.getEmailTo(), file);
            }
            return file;
        }
    }

    private void sendEmail(String emailTo, File file) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("donotreply@steelerose.co.uk", "Steele Rose Support");
        helper.setTo(emailTo);
        System.out.println("emailTo===" + emailTo);
        String subject = "Deceased details and bank initial letter";

        String content = "<p>Hello,</p>"
                + "<p>Please find the attached bank initial letter with deceased details.</p>"
                + "<p>Yours faithfully</p>" + getUsername();

        helper.setSubject(subject);

        helper.setText(content, true);
        FileSystemResource res = new FileSystemResource(/*new File("src/output.docx")*/ file);
        System.out.println("res.getFilename()===" + res.getFilename());
        helper.addAttachment("deceased details.docx", res);

        mailSender.send(message);

        boolean result = file.delete();
        if (result) {
            System.out.println("File is success delete.");
        } else {
            System.out.println("File doesn't exist.");
        }
    }

    public void writeDoc(LetterDto letterDto) throws Exception {
        //Blank Document
        XWPFDocument document= new XWPFDocument();

        //Write the Document in file system
        File file = new File("src/Deceased Details.docx");
        FileOutputStream out = new FileOutputStream(file);

        //create paragraph
        XWPFParagraph paragraph = document.createParagraph();

        XWPFRun titleRun = paragraph.createRun();
        titleRun.setText("Deceased Details");
        titleRun.setBold(true);
        titleRun.setFontFamily("Calibri (Body)");
      //  titleRun.addCarriageReturn();

        XWPFRun listRun = paragraph.createRun();
        listRun.setText("Deceased Name: \t" + letterDto.getDeceasedName());
        listRun.addBreak();
        listRun.setText("Deceased Address: \t\t" + letterDto.getDeceasedAddress());
        listRun.addBreak();
        listRun.setText("Date of birth: \t\t" + letterDto.getDoBirth());
        listRun.addBreak();
        listRun.setText("Date of death: \t\t" + letterDto.getDoDeath());
        listRun.addBreak();
        listRun.setText("Marital status: \t\t" + letterDto.getMaritalStatus());
        listRun.addBreak();
        listRun.setText("Date of Will (if applicable): " + letterDto.getDoWill());

        titleRun.addCarriageReturn();
        titleRun.addCarriageReturn();


        XWPFRun titleRun2 = paragraph.createRun();
        titleRun2.addCarriageReturn();
        titleRun2.addCarriageReturn();
        titleRun2.addCarriageReturn();
        titleRun2.setText("Executors Details");
        titleRun2.setBold(true);
        titleRun2.setFontFamily("Calibri (Body)");

        XWPFRun listRun2 = paragraph.createRun();
        listRun2.addBreak();
        listRun2.addBreak();
        listRun2.setText("Name: \t\t" + letterDto.getExecutorName());
        listRun2.addBreak();
        listRun2.setText("Address: \t\t" + letterDto.getExecutorAddress());
        listRun2.addBreak();
        listRun2.setText("Relationship: \t\t" + letterDto.getRelationship());
        listRun2.addBreak();
        listRun2.setText("Contact Number: \t\t" + letterDto.getContactNumber());


        XWPFParagraph paragraph2 = document.createParagraph();
        paragraph2.setPageBreak(true);
        String[] address = null;
        if (letterDto.getBankAddress() !=null) {
            address = letterDto.getBankAddress().split("\n");
        }
        System.out.println("address" + address.length);

        XWPFRun bankRun = paragraph2.createRun();
        bankRun.setText("Natwest");
        bankRun.setFontFamily("Times New Roman");
        bankRun.addBreak();
        for (int i=0; i < 5; i++) {
            if (i < address.length) {
                bankRun.setText(address[i]);
                bankRun.addBreak();
            } else {
                bankRun.addBreak();
            }
        }

        bankRun.addBreak();
        bankRun.addBreak();
        bankRun.addBreak();

        bankRun.setText("Our Ref:     /" + letterDto.getOurReference());
        bankRun.addBreak();
        bankRun.setText("Your Ref:     " + letterDto.getYourReference());
        bankRun.addCarriageReturn();
        bankRun.addCarriageReturn();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        bankRun.setText(sdf.format(new Date()));
        bankRun.addCarriageReturn();
        bankRun.addCarriageReturn();
        bankRun.setText("Dear Sirs,");
        bankRun.addCarriageReturn();
        bankRun.addCarriageReturn();

        XWPFRun letterRun = paragraph2.createRun();
        letterRun.setBold(true);
        letterRun.setText("Estate of " + letterDto.getEstateOf() + " Deceased.");
        letterRun.setFontFamily("Times New Roman");
        letterRun.addBreak();
        letterRun.setText("Late of " + letterDto.getDeceasedName());
        letterRun.addCarriageReturn();
        letterRun.addCarriageReturn();

        XWPFRun letterRun1 = paragraph2.createRun();
        letterRun1.setFontFamily("Times New Roman");
        letterRun1.setText("We act in the administration of the estate of the late " + letterDto.getDeceasedName() +
                " who died on the " + letterDto.getDoDeath() + ". We enclose a death certificate ");
        XWPFRun letterRun2 = paragraph2.createRun();
        letterRun2.setFontFamily("Times New Roman");
        letterRun2.setBold(true);
        letterRun2.setText("for noting in your records and to return to us. ");

        letterRun2.addCarriageReturn();
        letterRun2.addCarriageReturn();

        letterRun1.setText("We understand that the deceased held the following account with your organisation: -");

        letterRun1.addCarriageReturn();
        letterRun1.addCarriageReturn();

        letterRun2.setText("Account Number");
        letterRun2.addBreak();
        letterRun2.setText(letterDto.getAccountNumber());



        document.write(out);
        out.close();
     //   System.out.println("hotel.docx written successully" + file.getAbsolutePath());
    }

    private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}

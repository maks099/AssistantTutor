package com.example.assistanttutor.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import androidx.core.content.FileProvider;
import com.example.assistanttutor.R;
import com.example.assistanttutor.database.objects.Lesson;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PdfHelper {

    private Context context;
    private Activity activity;
    private File myFile;

    public PdfHelper(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void createPdf(ArrayList<Lesson> lessons, String courseName) throws IOException, DocumentException {
        File pdfFolder = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);

        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
        myFile = new File(pdfFolder + "/" + timeStamp + ".pdf");
        OutputStream output = new FileOutputStream(myFile);

        Document document = new Document();

        PdfWriter.getInstance(document, output);

        document.open();
        BaseFont bf = BaseFont.createFont("/assets/fonts/Temporarium.otf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font f1 = new Font(bf, 14);
        Paragraph mainTitle = new Paragraph(context.getString(R.string.report), f1);
        mainTitle.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(mainTitle);

        Paragraph course = new Paragraph(context.getString(R.string.course) + " " + courseName, f1);
        course.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(course);

        Paragraph student = new Paragraph(context.getString(R.string.student) + " " + lessons.get(0).getName(), f1);
        student.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(student);

        document.add(new Paragraph(""));

        PdfPTable table = new PdfPTable(4);
        PdfPCell c1 = new PdfPCell(new Phrase(context.getString(R.string.date), f1));
        PdfPCell c2 = new PdfPCell(new Phrase(context.getString(R.string.theme2), f1));
        PdfPCell c3 = new PdfPCell(new Phrase(context.getString(R.string.score2), f1));
        PdfPCell c4 = new PdfPCell(new Phrase(context.getString(R.string.cost2), f1));
        table.addCell(c1);
        table.addCell(c2);
        table.addCell(c3);
        table.addCell(c4);

        for(Lesson lesson : lessons){
            c1 = new PdfPCell(new Phrase(lesson.getDate(), f1));
            table.addCell(c1);
            c2 = new PdfPCell(new Phrase(lesson.getTheme(), f1));
            table.addCell(c2);
            c3 = new PdfPCell(new Phrase(lesson.getScore()+"", f1));
            table.addCell(c3);
            c4 = new PdfPCell(new Phrase(lesson.getCost() + "", f1));
            table.addCell(c4);
        }

        document.add(table);

        //Step 5: Close the document
        document.close();
        promptForNextAction();
    }



    private void promptForNextAction()
    {
        final String[] options = { context.getString(R.string.send), context.getString(R.string.preview),
                context.getString(R.string.cancel) };

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(context.getString(R.string.savedWhatNext));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals(context.getString(R.string.send))){
                    emailNote();
                }else if (options[which].equals(context.getString(R.string.preview))){
                    viewPdf();
                }else if (options[which].equals(context.getString(R.string.cancel))){
                    dialog.dismiss();
                }
            }
        });

        builder.show();

    }

    private void emailNote()
    {
        Intent email = new Intent(Intent.ACTION_SEND);
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName()+".provider",myFile);
        email.putExtra(Intent.EXTRA_STREAM, uri);
        email.setType("application/pdf");
        context.startActivity(email);
    }

    private void viewPdf(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName()+".provider",myFile), "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);
    }
}

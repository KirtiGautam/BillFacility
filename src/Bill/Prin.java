package Bill;

import Bill.Data.Credentials;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.DecimalFormat;

class EnglishNumberToWords {

    private static final String[] tensNames = {
            "",
            " Ten",
            " Twenty",
            " Thirty",
            " Forty",
            " Fifty",
            " Sixty",
            " Seventy",
            " Eighty",
            " Ninety"
    };

    private static final String[] numNames = {
            "",
            " One",
            " Two",
            " Three",
            " Four",
            " Five",
            " Six",
            " Seven",
            " Eight",
            " Nine",
            " Ten",
            " Eleven",
            " Twelve",
            " Thirteen",
            " Fourteen",
            " Fifteen",
            " Sixteen",
            " Seventeen",
            " Eighteen",
            " Nineteen"
    };

    private EnglishNumberToWords() {
    }

    private static String convertLessThanOneThousand(int number) {
        String soFar;

        if (number % 100 < 20) {
            soFar = numNames[number % 100];
            number /= 100;
        } else {
            soFar = numNames[number % 10];
            number /= 10;

            soFar = tensNames[number % 10] + soFar;
            number /= 10;
        }
        if (number == 0) return soFar;
        return numNames[number] + " Hundred" + soFar;
    }


    public static String convert(long number) {
        // 0 to 999 999 999 999
        if (number == 0) {
            return "zero";
        }

        String snumber = Long.toString(number);

        // pad with "0"
        String mask = "000000000000";
        DecimalFormat df = new DecimalFormat(mask);
        snumber = df.format(number);

        // XXXnnnnnnnnn
        int billions = Integer.parseInt(snumber.substring(3, 5));
        // nnnXXXnnnnnn
        int millions = Integer.parseInt(snumber.substring(5, 7));
        // nnnnnnXXXnnn
        int hundredThousands = Integer.parseInt(snumber.substring(7, 9));
        // nnnnnnnnnXXX
        int thousands = Integer.parseInt(snumber.substring(9, 12));

        String tradBillions;
        switch (billions) {
            case 0:
                tradBillions = "";
                break;
            case 1:
                tradBillions = convertLessThanOneThousand(billions)
                        + " Crore ";
                break;
            default:
                tradBillions = convertLessThanOneThousand(billions)
                        + " Crore ";
        }
        String result = tradBillions;

        String tradMillions;
        switch (millions) {
            case 0:
                tradMillions = "";
                break;
            case 1:
                tradMillions = convertLessThanOneThousand(millions)
                        + " Lakh ";
                break;
            default:
                tradMillions = convertLessThanOneThousand(millions)
                        + " Lakh ";
        }
        result = result + tradMillions;

        String tradHundredThousands;
        switch (hundredThousands) {
            case 0:
                tradHundredThousands = "";
                break;
            case 1:
                tradHundredThousands = "One Thousand ";
                break;
            default:
                tradHundredThousands = convertLessThanOneThousand(hundredThousands)
                        + " Thousand ";
        }
        result = result + tradHundredThousands;

        String tradThousand;
        tradThousand = convertLessThanOneThousand(thousands);
        result = result + tradThousand;

        // remove extra spaces!
        return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
    }
}


public class Prin {

    private static boolean check(String bill_no) throws Exception{
        Connection connection= DriverManager.getConnection("jdbc:sqlite:Billsdata\\Bills.db");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT Miscellaneous FROM bills WHERE Bill_no='"+bill_no+"'");
        while (resultSet.next()){
            if(!resultSet.getString("Miscellaneous").equals(""))
                return true;
        }
        statement.close();
        connection.close();
        return false;
    }

    public static boolean print_pdf(String bill_no){
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 24,
                Font.BOLD);
        try {
            boolean miscFlag=check(bill_no);
            Connection connection = DriverManager.getConnection("jdbc:sqlite:Billsdata\\Bills.db");
            Statement statement = connection.createStatement();
            Document document = new Document(PageSize.A4);
            FileChooser fileChooser=new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("pdf doc(*.pdf)", "*.pdf"));

            File file=fileChooser.showSaveDialog(Main.parentWindow);
            if(file==null)
                return false;
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM billData WHERE BillNo='" + bill_no + "'");
            //Title
            Paragraph paragraph = new Paragraph("INVOICE", catFont);
            paragraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(paragraph);
            catFont = new Font(Font.FontFamily.UNDEFINED, 28, Font.BOLD);
            paragraph = new Paragraph("SM CARGO MOVERS", catFont);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            paragraph = new Paragraph(Credentials.getInstance().getAddress());
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            paragraph = new Paragraph("Mobile: " + Credentials.getInstance().getPhone() +
                    "                      E-mail:" + Credentials.getInstance().getE_mail());
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            paragraph = new Paragraph("  ");
            document.add(paragraph);
            PdfPTable cred = new PdfPTable(1);
            cred.setWidthPercentage(34);
            cred.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cred.addCell(getcredCell("PAN: " + Credentials.getInstance().getPAN()));
            cred.addCell(getcredCell("GSTIN: " + Credentials.getInstance().getGST()));
            document.add(cred);
            paragraph = new Paragraph("  ");
            document.add(paragraph);
            PdfPTable det = new PdfPTable(2);
            det.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            det.setTotalWidth(PageSize.A4.getWidth());
            det.setWidthPercentage(100);
            det.addCell(getCellH("Bill To"));
            if(resultSet.getString("VendorNo").equals(""))
                det.addCell("   ");
            else
                det.addCell(getdetailCell("Vendor No.: " + resultSet.getString("VendorNo")));
            det.setHeaderRows(1);
            String cons[]=resultSet.getString("Consignor").split("\n");
            det.addCell(getdetailCell(cons[0]));
            String bill="Bill No.: " + resultSet.getString("BillNo");
            String date="Date: " + resultSet.getString("BillDate");
            det.addCell(getdetailCell(date));
            det.addCell(getdetailCell(cons[1]));
            resultSet = statement.executeQuery("SELECT * FROM bills WHERE Bill_no='" + bill_no + "'");
            det.addCell(getdetailCell(bill));
            document.add(det);


            //Table
            PdfPTable billTable;
            if(miscFlag) {
                billTable = new PdfPTable(10);
                billTable.setWidths(new float[]{0.5F, 2.7F, 1.2F, 1.1F,1.6F,1F, 0.5F, 1.2F,1,2F});
            }
            else{
                billTable = new PdfPTable(9);
                billTable.setWidths(new float[]{3, 29, 9, 8, 10, 4, 4, 10, 9});
            }
            billTable.setTotalWidth(PageSize.A4.getWidth());
            billTable.setWidthPercentage(100);
            billTable.setSpacingBefore(30.0f);
            billTable.addCell(getBillHeaderCell("S.No"));
            billTable.addCell(getBillHeaderCell("Description"));
            billTable.addCell(getBillHeaderCell("Date"));
            billTable.addCell(getBillHeaderCell("Invoice"));
            billTable.addCell(getBillHeaderCell("Consignee"));
            if (miscFlag)
                billTable.addCell(getBillHeaderCell("Misc."));
            billTable.addCell(getBillHeaderCell("GR"));
            billTable.addCell(getBillHeaderCell("Pkgs."));
            billTable.addCell(getBillHeaderCell("Weight"));
            billTable.addCell(getBillHeaderCell("Amount"));
            int sNo = 1,total=0;
            while (resultSet.next()) {
                billTable.addCell(getBillRowCell(String.valueOf(sNo)));
                billTable.addCell(getBillRowCell(resultSet.getString("Name")));
                billTable.addCell(getBillRowCell(resultSet.getString("Date")));
                billTable.addCell(getBillRowCell(resultSet.getString("Invoice")));
                billTable.addCell(getBillRowCell(resultSet.getString("Consignee")));
                if (miscFlag)
                    billTable.addCell(getBillRowCell(resultSet.getString("Miscellaneous")));
                billTable.addCell(getBillRowCell(resultSet.getString("GR")));
                billTable.addCell(getBillRowCell(resultSet.getString("NOP")));
                billTable.addCell(getBillRowCell(resultSet.getString("Weight")));
                billTable.addCell(getBillRowCell(resultSet.getString("Amount")));
                sNo+=1;
                total+=resultSet.getInt("Amount");
            }

            billTable.addCell(getdescCell("     "));
            billTable.addCell(getdescCell("     "));
            billTable.addCell(getdescCell("     "));
            if(miscFlag)
                billTable.addCell(getdescCell("     "));
            billTable.addCell(getdescCell("     "));
            billTable.addCell(getdescCell("     "));
            billTable.addCell(getdescCell("      "));
            billTable.addCell(getdescCell("     "));
            PdfPCell c=getdescCell("Total:");
            c.setBorderWidthRight(0);
            c.setBorderWidthTop(1);
            billTable.addCell(c);

            c=getdescCell("Rs. "+total);
            c.setBorderWidthLeft(0);
            c.setBorderWidthTop(1);
            billTable.addCell(c);
            document.add(billTable);
            catFont=new Font(Font.FontFamily.UNDEFINED, 14, Font.BOLD);
            paragraph=new Paragraph("   Amount in words: "+EnglishNumberToWords.convert(total)+" Only",catFont);
            paragraph.setAlignment(Element.ALIGN_LEFT);
            document.add(paragraph);
            //Banking
            document.add(Chunk.NEWLINE);
            catFont=new Font(Font.FontFamily.UNDEFINED, 16, Font.BOLD);
            paragraph=new Paragraph("Bank Details:",catFont);
            paragraph.setAlignment(Element.ALIGN_LEFT);
            document.add(paragraph);
            paragraph=new Paragraph("ACCOUNT NAME : "+Credentials.getInstance().getAcName()+"\nBANK NAME : "+Credentials.getInstance().getBankName()
                                    +"\nACCOUNT NUMBER : "+Credentials.getInstance().getAcNo()+"\nIFSC CODE : "+Credentials.getInstance().getIfscCode());
            paragraph.setAlignment(Element.ALIGN_LEFT);
            document.add(paragraph);

            PdfContentByte cb = pdfWriter.getDirectContent();
            FontSelector fs=new FontSelector();
            Font font=new Font(Font.FontFamily.UNDEFINED,12, Font.BOLD);
            fs.addFont(font);
            Phrase footer = fs.process("For SM Cargo Movers");
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    footer,
                    (document.right() -90),
                    document.bottom() +20, 0);
            footer = new Phrase("Authorised Signatory");
            footer.setFont(new Font(Font.FontFamily.TIMES_ROMAN,15, Font.BOLD));
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    footer,
                    (document.right() -90),
                    document.bottom() , 0);
            statement.close();
            connection.close();
            document.close();
        }catch (Exception e){e.printStackTrace();}
        return true;

    }

    public static PdfPCell getcredCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    public static PdfPCell getBillHeaderCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 11);
        font.setColor(BaseColor.WHITE);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    public static PdfPCell getBillRowCell(String text) {
        PdfPCell cell = new PdfPCell(new Paragraph(text));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        return cell;
    }

    public static PdfPCell getCellH(String text) {
        FontSelector fs=new FontSelector();
        Font font=new Font(Font.FontFamily.UNDEFINED,12, Font.BOLD);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthBottom(0);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(5.0f);
        cell.setPaddingLeft(20.0f);
        return cell;
    }

    public static PdfPCell getdetailCell(String text) {
        Phrase phrase = new Phrase(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthBottom(0);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(5.0f);
        cell.setPaddingLeft(20.0f);
        return cell;
    }

    public static PdfPCell getdescCell(String text) {
        PdfPCell cell = new PdfPCell(new Paragraph(text));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthTop(0);
        return cell;
    }

}

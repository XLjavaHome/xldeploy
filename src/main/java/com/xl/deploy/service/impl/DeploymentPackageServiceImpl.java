package com.xl.deploy.service.impl;

import com.xl.deploy.entity.DeploymentEntity;
import com.xl.deploy.service.DeploymentPackageService;
import com.xl.util.DateUtil;
import com.xl.util.FileUtil;
import com.xl.util.StringUtil;
import java.io.*;
import java.math.BigInteger;
import java.util.List;
import javax.swing.JOptionPane;
import org.antlr.v4.runtime.misc.NotNull;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
import org.springframework.stereotype.Service;

/**
 * Created with 徐立.
 *
 * @author 徐立
 * @date 2019-04-13
 * @time 15:51
 * To change this template use File | Settings | File Templates.
 */
@Service
public class DeploymentPackageServiceImpl implements DeploymentPackageService {
    @Override
    public void createFile(DeploymentEntity entity) throws IOException {
        String author = entity.getAuthor();
        String codeTextText = entity.getCodeString();
        String directoryName = entity.isFlag() ? String.format(DateUtil.formatLocalDate() + "_%s_" + author + "_wh", entity.getTaskName()) : String
                .format(DateUtil.formatLocalDate() + "_%s_" + author + "_wh", "BUG修复");
        File publishPackageNameDirectory = FileUtil.createTempDirectoy(directoryName);
        initCodeText(codeTextText, publishPackageNameDirectory);
        initDoc(entity, publishPackageNameDirectory);
        FileUtil.open(publishPackageNameDirectory);
    }
    
    /**
     * 生成code.txt
     *
     * @param codeTextText
     * @param publishPackageNameDirectory
     * @throws IOException
     */
    private void initCodeText(String codeTextText, File publishPackageNameDirectory) throws IOException {
        File code = new File(publishPackageNameDirectory, "code.txt");
        FileUtil.write(code, codeTextText);
    }
    
    /**
     * 生成word文档
     *
     * @param entity
     * @param publishPackageNameDirectory
     * @throws IOException
     */
    private void initDoc(DeploymentEntity entity, File publishPackageNameDirectory) throws FileNotFoundException {
        //生成部署文档
        try (XWPFDocument doc = new XWPFDocument()) {
            XWPFParagraph docParagraph = doc.createParagraph();
            XWPFRun xwpfRun = docParagraph.createRun();
            if (entity.isFlag()) {
                //    任务名称
                xwpfRun.setText("[任务名称]");
                addTitleStyle(xwpfRun);
                xwpfRun.addBreak();
                addDocContent(docParagraph, entity.getTaskName());
            } else {
                xwpfRun.setText("[BUG修复]");
                addTitleStyle(xwpfRun);
                String docString = entity.getDocString();
                if (StringUtil.isNotEmpty(docString)) {
                    String[] split = StringUtils.split(docString, '\n');
                    //是否生成表格
                    boolean isInitWordTable = true;
                    for (int i = 0; i < split.length; i++) {
                        String[] linesStrings = StringUtils.split(split[i], ':');
                        if (linesStrings.length != 2) {
                            isInitWordTable = false;
                            break;
                        }
                    }
                    if (isInitWordTable) {
                        XWPFTable wordTable = doc.createTable(split.length, 2);
                        List<XWPFTableRow> rows = wordTable.getRows();
                        for (int i = 0; i < rows.size(); i++) {
                            XWPFTableRow xwpfTableRow = rows.get(i);
                            //每一行的内容
                            //每一行的单元格
                            List<XWPFTableCell> tableCells = xwpfTableRow.getTableCells();
                            //左侧单元格
                            XWPFTableCell leftCell = tableCells.get(0);
                            CTTcPr cellPr11 = leftCell.getCTTc().addNewTcPr();
                            cellPr11.addNewVAlign().setVal(STVerticalJc.CENTER);
                            cellPr11.addNewTcW().setW(BigInteger.valueOf(2000));
                            String[] linesStrings = StringUtils.split(split[i], ':');
                            leftCell.setText(linesStrings[0]);
                            XWPFTableCell rightCell = tableCells.get(1);
                            rightCell.setText(linesStrings[1]);
                            CTTcPr cellPr1 = rightCell.getCTTc().addNewTcPr();
                            cellPr1.addNewVAlign().setVal(STVerticalJc.CENTER);
                            cellPr1.addNewTcW().setW(BigInteger.valueOf(6000));
                        }
                    } else {
                        //不是BUG的话在doc文档中加
                        xwpfRun.addBreak();
                        for (int i = 0; i < split.length; i++) {
                            xwpfRun = addDocContent(docParagraph, split[i]);
                            xwpfRun.addBreak();
                        }
                    }
                }
            }
            docParagraph = doc.createParagraph();
            XWPFRun run = docParagraph.createRun();
            run.addBreak();
            addTitleStyle(run);
            run.setText("[部署说明]");
            run.addBreak();
            XWPFRun run1 = docParagraph.createRun();
            run1.setFontSize(14);
            XWPFRun xwpfRun1 = addDocContent(docParagraph, "1.更新code.txt。");
            xwpfRun1.addBreak();
            addDocContent(docParagraph, "2.重启服务。");
            doc.write(new BufferedOutputStream(new FileOutputStream(new File(publishPackageNameDirectory, "部署说明.docx"))));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "文件可能被占用", "提示", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * 标题样式
     *
     * @param xwpfRun
     */
    public void addTitleStyle(XWPFRun xwpfRun) {
        xwpfRun.setFontFamily("宋体");
        xwpfRun.setFontSize(22);
        xwpfRun.setBold(true);
    }
    
    /**
     * 添加内容
     *
     * @param docParagraph
     * @param content
     * @return
     */
    @NotNull
    public XWPFRun addDocContent(XWPFParagraph docParagraph, String content) {
        XWPFRun xwpfRun = docParagraph.createRun();
        xwpfRun.setText(content);
        xwpfRun.setFontSize(12);
        return xwpfRun;
    }
}

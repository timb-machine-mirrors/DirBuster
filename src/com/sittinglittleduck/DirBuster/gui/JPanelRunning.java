/*
 * JPanelRunning.java
 *
 * Copyright 2007 James Fisher
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301 USA
 */
package com.sittinglittleduck.DirBuster.gui;

import com.sittinglittleduck.DirBuster.DirToCheck;
import com.sittinglittleduck.DirBuster.ExtToCheck;
import com.sittinglittleduck.DirBuster.Manager;
import com.sittinglittleduck.DirBuster.Result;
import com.sittinglittleduck.DirBuster.gui.tableModels.ResultsTableModel;
import com.sittinglittleduck.DirBuster.gui.JTableTree.JTreeTable;
import com.sittinglittleduck.DirBuster.gui.JTableTree.TreeTableModelAdapter;
import com.sittinglittleduck.DirBuster.gui.tableModels.ErrorTableModel;
import com.sittinglittleduck.DirBuster.gui.tree.ResultsNode;
import com.sittinglittleduck.DirBuster.gui.tree.ResultsTableTreeModel;
import com.sittinglittleduck.DirBuster.gui.tableModels.ScanInfoTableModel;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import java.util.ArrayList;
import edu.stanford.ejalbert.BrowserLauncher;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

public class JPanelRunning extends javax.swing.JPanel implements ClipboardOwner
{

    StartGUI parent;
    public ResultsTableModel resultsTableModel;
    public ErrorTableModel errorTableModel;
    private DirToCheck localDirToCheck;
    private Manager manager = Manager.getInstance();
    
    public ResultsNode rootNode;
    public JTreeTable jTableTreeResults;
    public ResultsTableTreeModel tableTreeModel;
    public ScanInfoTableModel scanInfoTableModel;

    /** Creates new form JPanelRunning */
    public JPanelRunning(StartGUI Parent)
    {
        initComponents();
        parent = Parent;

        jTableResults.setAutoResizeMode(jTableResults.AUTO_RESIZE_NEXT_COLUMN);


        /*
         * Error Handling added to deal with java 1.6 only function
         * Thus 
         */
        try
        {
            //Enable the auto row sorter
            jTableResults.setAutoCreateRowSorter(true);
        }
        catch (java.lang.NoSuchMethodError e)
        {
            System.err.println("Warning java version below 1.6: Results row sorting is disabled");
        }

        TableColumnModel colModel = jTableResults.getColumnModel();
        //colModel.getColumn(0).setPreferredWidth(300);
        colModel.getColumn(1).setPreferredWidth(250);
        colModel.getColumn(2).setPreferredWidth(25);
        //colModel.getColumn(3).setPreferredWidth(40);
        //colModel.getColumn(4).setPreferredWidth(50);
        colModel.getColumn(0).setMinWidth(20);
        colModel.getColumn(0).setPreferredWidth(20);
        
        
        /*
         * add the table view model
         */
        
        tableTreeModel = new ResultsTableTreeModel();
        //scanInfoTableModel = (ScanInfoTableModel) jTableScanInformation.getModel();
        jTableTreeResults = new JTreeTable(tableTreeModel);
        jTableTreeResults.getTree().setShowsRootHandles(true);
        
        jTableTreeResults.addMouseListener(new java.awt.event.MouseAdapter()
        {
            
            public void mouseClicked(java.awt.event.MouseEvent e)
            {
                
                if(e.getButton() == 2 || e.getButton() == 3)
                {
                    if(jTableTreeResults.getSelectedRowCount() != 0)
                    {
                        jPopupMenuTreeTableResults.show(e.getComponent(),e.getX(), e.getY());
                    }
                    
                }
                 
            }
            
        });
        
        
        //jTableTreeResults.setModel();
        jScrollPaneTreeView.setViewportView(jTableTreeResults);

        errorTableModel = (ErrorTableModel) jTableErrors.getModel();
        
        //jTableScanInformation.getColumnModel().getColumn(1).setCellRenderer(new ProgressRenderer());
        //jTableScanInformation.getColumnModel().getColumn(2).setCellEditor(new JButtonCellEditor());
        //jTableScanInformation.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
        //jTableScanInformation.getColumnModel().getColumn(0).setPreferredWidth(400);
        //jTableScanInformation.getColumnModel().getColumn(1).setPreferredWidth(100);

    }

    public void lostOwnership(Clipboard parClipboard, Transferable parTransferable)
    {

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenuOpenBrowser = new javax.swing.JPopupMenu();
        jMenuItemOpen = new javax.swing.JMenuItem();
        jMenuItemView = new javax.swing.JMenuItem();
        jMenuItemCopy = new javax.swing.JMenuItem();
        jPopupMenuTreeTableResults = new javax.swing.JPopupMenu();
        jMenuItemOpenInBrowser = new javax.swing.JMenuItem();
        jMenuItemViewResponse = new javax.swing.JMenuItem();
        jMenuItemCopyURL = new javax.swing.JMenuItem();
        jButtonBack = new javax.swing.JButton();
        jButtonStop = new javax.swing.JButton();
        jLabelCurrentSpeed = new javax.swing.JLabel();
        jLabelAverageSpeed = new javax.swing.JLabel();
        jLabelTotalRequests = new javax.swing.JLabel();
        jButtonReport = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabelNumThreads = new javax.swing.JLabel();
        jTextFieldNumberOfThreads = new javax.swing.JTextField();
        jButtonChangeThreads = new javax.swing.JButton();
        jLabelCurrentTarget = new javax.swing.JLabel();
        jLabelTimeLeft = new javax.swing.JLabel();
        jLabelParseQueueLength = new javax.swing.JLabel();
        jToggleButtonPause = new javax.swing.JToggleButton();
        jTabbedPaneViewResults = new javax.swing.JTabbedPane();
        jScrollPaneScanInformation = new javax.swing.JScrollPane();
        jPanelScanInfoBase = new javax.swing.JPanel();
        jScrollPaneListResults = new javax.swing.JScrollPane();
        //ListSelectionModel rowSM = jTableBaseCase.getSelectionModel();
        List resultsData = new ArrayList();
        jTableResults = new javax.swing.JTable();
        jScrollPaneTreeView = new javax.swing.JScrollPane();
        jScrollPaneErrorView = new javax.swing.JScrollPane();
        jTableErrors = new javax.swing.JTable();

        jMenuItemOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sittinglittleduck/DirBuster/gui/icons/internet-web-browser.png"))); // NOI18N
        jMenuItemOpen.setText("Open In Browser");
        jMenuItemOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOpenActionPerformed(evt);
            }
        });
        jPopupMenuOpenBrowser.add(jMenuItemOpen);

        jMenuItemView.setText("View Response");
        jMenuItemView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemViewActionPerformed(evt);
            }
        });
        jPopupMenuOpenBrowser.add(jMenuItemView);

        jMenuItemCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sittinglittleduck/DirBuster/gui/icons/edit-copy.png"))); // NOI18N
        jMenuItemCopy.setText("Copy URL");
        jMenuItemCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCopyActionPerformed(evt);
            }
        });
        jPopupMenuOpenBrowser.add(jMenuItemCopy);

        jMenuItemOpenInBrowser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sittinglittleduck/DirBuster/gui/icons/internet-web-browser.png"))); // NOI18N
        jMenuItemOpenInBrowser.setText("Open In Browser");
        jMenuItemOpenInBrowser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOpenInBrowserActionPerformed(evt);
            }
        });
        jPopupMenuTreeTableResults.add(jMenuItemOpenInBrowser);

        jMenuItemViewResponse.setText("View Response");
        jMenuItemViewResponse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemViewResponseActionPerformed(evt);
            }
        });
        jPopupMenuTreeTableResults.add(jMenuItemViewResponse);

        jMenuItemCopyURL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sittinglittleduck/DirBuster/gui/icons/edit-copy.png"))); // NOI18N
        jMenuItemCopyURL.setText("Copy URL");
        jMenuItemCopyURL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCopyURLActionPerformed(evt);
            }
        });
        jPopupMenuTreeTableResults.add(jMenuItemCopyURL);

        jButtonBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sittinglittleduck/DirBuster/gui/icons/go-previous.png"))); // NOI18N
        jButtonBack.setText("Back");
        jButtonBack.setEnabled(false);
        jButtonBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackActionPerformed(evt);
            }
        });

        jButtonStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sittinglittleduck/DirBuster/gui/icons/media-playback-stop.png"))); // NOI18N
        jButtonStop.setText("Stop");
        jButtonStop.setToolTipText("Click to stop the application");
        jButtonStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStopActionPerformed(evt);
            }
        });

        jLabelCurrentSpeed.setText("Current Speed: ");

        jLabelAverageSpeed.setText("Average Speed:");
        jLabelAverageSpeed.setToolTipText("(T) = Total average, (C) = Average of last 10 seconds");

        jLabelTotalRequests.setText("Total Requests:");

        jButtonReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sittinglittleduck/DirBuster/gui/icons/txt.png"))); // NOI18N
        jButtonReport.setText("Report");
        jButtonReport.setEnabled(false);
        jButtonReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReportActionPerformed(evt);
            }
        });

        jLabel1.setText("(Select and right click for more options)");

        jLabelNumThreads.setText("Current number of running threads");

        jTextFieldNumberOfThreads.setToolTipText("Please enter the required number of threads");

        jButtonChangeThreads.setText("Change");
        jButtonChangeThreads.setToolTipText("Click to change the number of running threads");
        jButtonChangeThreads.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonChangeThreadsActionPerformed(evt);
            }
        });

        jLabelCurrentTarget.setText("TODO: display current target");

        jLabelTimeLeft.setText("Time To Finish:");

        jLabelParseQueueLength.setText("Parse Queue Size:");
        jLabelParseQueueLength.setToolTipText("Total number items in the to be parsed queue");

        jToggleButtonPause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sittinglittleduck/DirBuster/gui/icons/media-playback-pause.png"))); // NOI18N
        jToggleButtonPause.setText("Pause");
        jToggleButtonPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonPauseActionPerformed(evt);
            }
        });

        jPanelScanInfoBase.setBackground(new java.awt.Color(255, 255, 255));
        jPanelScanInfoBase.setLayout(new javax.swing.BoxLayout(jPanelScanInfoBase, javax.swing.BoxLayout.Y_AXIS));
        jScrollPaneScanInformation.setViewportView(jPanelScanInfoBase);

        jTabbedPaneViewResults.addTab("Scan Info: R: 0 P: 0 C: 0", new javax.swing.ImageIcon(getClass().getResource("/com/sittinglittleduck/DirBuster/gui/icons/information.png")), jScrollPaneScanInformation, "R: Running tasks P: Paused Tasks C: Completed Tasks"); // NOI18N

        jScrollPaneListResults.setBackground(new java.awt.Color(255, 255, 255));

        jTableResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsTableModel = new ResultsTableModel();
        jTableResults.setModel(resultsTableModel);
        jTableResults.addMouseListener(new java.awt.event.MouseAdapter()
            {
                //TODO add furture feature for right click to connect proxy tunnel to!

                public void mouseClicked(java.awt.event.MouseEvent e)
                {

                    //System.out.println(e.getButton());
                    if(e.getButton() == 2 || e.getButton() == 3)
                    {
                        if(jTableResults.getSelectedRowCount() != 0)
                        {
                            jPopupMenuOpenBrowser.show(e.getComponent(),e.getX(), e.getY());
                        }

                    }

                }

            });
            jScrollPaneListResults.setViewportView(jTableResults);

            jTabbedPaneViewResults.addTab("Results - List View: Dirs: 0 Files: 0 ", jScrollPaneListResults);

            jScrollPaneTreeView.setBackground(new java.awt.Color(255, 255, 255));
            jTabbedPaneViewResults.addTab("Results - Tree View", jScrollPaneTreeView);

            jScrollPaneErrorView.setBackground(new java.awt.Color(255, 255, 255));
            jScrollPaneErrorView.setDoubleBuffered(true);

            jTableErrors.setModel(new ErrorTableModel(jTableErrors));
            jScrollPaneErrorView.setViewportView(jTableErrors);

            jTabbedPaneViewResults.addTab("Errors: 0", new javax.swing.ImageIcon(getClass().getResource("/com/sittinglittleduck/DirBuster/gui/icons/dialog-warning.png")), jScrollPaneErrorView); // NOI18N

            org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
            this.setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                            .add(12, 12, 12)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(layout.createSequentialGroup()
                                    .add(jLabelCurrentSpeed, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 295, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 192, Short.MAX_VALUE)
                                    .add(jLabel1))
                                .add(layout.createSequentialGroup()
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(jButtonBack, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(jToggleButtonPause)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(jButtonStop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 94, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 298, Short.MAX_VALUE)
                                    .add(jButtonReport, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 106, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(layout.createSequentialGroup()
                                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jLabelTimeLeft, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jLabelAverageSpeed, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jLabelParseQueueLength, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jLabelTotalRequests, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .add(165, 165, 165)
                                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(layout.createSequentialGroup()
                                            .add(jTextFieldNumberOfThreads, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 137, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                            .add(jButtonChangeThreads))
                                        .add(jLabelNumThreads)))))
                        .add(layout.createSequentialGroup()
                            .addContainerGap()
                            .add(jLabelCurrentTarget))
                        .add(layout.createSequentialGroup()
                            .addContainerGap()
                            .add(jTabbedPaneViewResults, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE)))
                    .addContainerGap())
            );

            layout.linkSize(new java.awt.Component[] {jButtonBack, jButtonReport, jButtonStop, jToggleButtonPause}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

            layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                    .addContainerGap()
                    .add(jLabelCurrentTarget)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(jTabbedPaneViewResults, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabelCurrentSpeed)
                        .add(jLabel1))
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                        .add(layout.createSequentialGroup()
                            .add(jLabelAverageSpeed)
                            .add(9, 9, 9)
                            .add(jLabelParseQueueLength)
                            .add(4, 4, 4))
                        .add(layout.createSequentialGroup()
                            .add(jLabelNumThreads)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                            .add(3, 3, 3)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(jButtonChangeThreads)
                                .add(jTextFieldNumberOfThreads, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(layout.createSequentialGroup()
                            .add(1, 1, 1)
                            .add(jLabelTotalRequests)))
                    .add(7, 7, 7)
                    .add(jLabelTimeLeft)
                    .add(5, 5, 5)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                            .add(2, 2, 2)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(jButtonBack)
                                .add(jButtonStop)
                                .add(jToggleButtonPause)))
                        .add(layout.createSequentialGroup()
                            .add(1, 1, 1)
                            .add(jButtonReport)))
                    .add(18, 18, 18))
            );
        }// </editor-fold>//GEN-END:initComponents

    private void jButtonChangeThreadsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonChangeThreadsActionPerformed
    {//GEN-HEADEREND:event_jButtonChangeThreadsActionPerformed
        int currentNumber = parent.manager.getWorkers().size();
        parent.jPanelSetup.jSliderThreads.setValue(Integer.parseInt(jTextFieldNumberOfThreads.getText()));
        //TODO: add try catch for number format exception

        if (Integer.parseInt(jTextFieldNumberOfThreads.getText()) > currentNumber)
        {
            int numberToAdd = Integer.parseInt(jTextFieldNumberOfThreads.getText()) - currentNumber;

            if (numberToAdd > 0)
            {
                parent.manager.addWrokers(numberToAdd);
            }
        }
        else if (Integer.parseInt(jTextFieldNumberOfThreads.getText()) < currentNumber)
        {
            int numberToRemove = currentNumber - Integer.parseInt(jTextFieldNumberOfThreads.getText());
            if (numberToRemove < currentNumber)
            {
                parent.manager.removeWorkers(numberToRemove);
            }
        }
        
    }//GEN-LAST:event_jButtonChangeThreadsActionPerformed

    private void jMenuItemViewActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemViewActionPerformed
    {//GEN-HEADEREND:event_jMenuItemViewActionPerformed

        int select = getSelectedItemInTable();
        /*
        if (jTableResults.getRowSorter() != null)
        {
            select = jTableResults.getRowSorter().convertRowIndexToModel(jTableResults.getSelectedRow());
        }
        else
        {
            select = jTableResults.getSelectedRow();
        }

         */

        new JDialogViewResponse(parent, true, manager.results.elementAt(select)).setVisible(true);

    }//GEN-LAST:event_jMenuItemViewActionPerformed

    private void jMenuItemCopyActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemCopyActionPerformed
    {//GEN-HEADEREND:event_jMenuItemCopyActionPerformed
        Clipboard clip = getToolkit().getSystemClipboard();

        int select = getSelectedItemInTable();
        /*
        if (jTableResults.getRowSorter() != null)
        {
            select = jTableResults.getRowSorter().convertRowIndexToModel(jTableResults.getSelectedRow());
        }
        else
        {
            select = jTableResults.getSelectedRow();
        }
        
         */
        StringSelection urlFromTable = new StringSelection(manager.results.elementAt(select).getItemFound().toString());
        clip.setContents(urlFromTable, this);
    }//GEN-LAST:event_jMenuItemCopyActionPerformed

    private void jButtonReportActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonReportActionPerformed
    {//GEN-HEADEREND:event_jButtonReportActionPerformed

        /*
         * Set the name of the report
         */
        
        
        /*
         * create a date in the required format
         */
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        /*
         * create the file location of the directory we wish to save
         */
        String reportName = dateFormat.format(date) + "-" + manager.getHost();
        
        parent.jPanelReport.jTextFieldReportName.setText("DirBuster-Report-" + reportName);
        
        parent.showReporting();



    }//GEN-LAST:event_jButtonReportActionPerformed

    private void jMenuItemOpenActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemOpenActionPerformed
    {//GEN-HEADEREND:event_jMenuItemOpenActionPerformed
        if (jTableResults.getSelectedRowCount() > 0)
        {
            try
            {
                int select = getSelectedItemInTable();
                /*
                if (jTableResults.getRowSorter() != null)
                {
                    select = jTableResults.getRowSorter().convertRowIndexToModel(jTableResults.getSelectedRow());
                }
                else
                {
                    select = jTableResults.getSelectedRow();
                }

                 */
                String tempURL = manager.results.elementAt(select).getItemFound().toString();
                BrowserLauncher launcher = new BrowserLauncher(null);
                launcher.openURLinBrowser(tempURL);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jMenuItemOpenActionPerformed

    private void jButtonStopActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonStopActionPerformed
    {//GEN-HEADEREND:event_jButtonStopActionPerformed
        parent.finished();
        jButtonReport.setEnabled(true);
        jButtonBack.setEnabled(true);
    }//GEN-LAST:event_jButtonStopActionPerformed

    public void enableReport()
    {
        jButtonReport.setEnabled(true);

    }

    private void jButtonBackActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonBackActionPerformed
    {//GEN-HEADEREND:event_jButtonBackActionPerformed
        resultsTableModel.clearData();
        errorTableModel.clearAllResults();
        parent.showSetup();
        
    }//GEN-LAST:event_jButtonBackActionPerformed

    private void jToggleButtonPauseActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jToggleButtonPauseActionPerformed
    {//GEN-HEADEREND:event_jToggleButtonPauseActionPerformed
        if(jToggleButtonPause.isSelected())
        {
            parent.pause();
        }
        else
        {
            parent.unPause();
        }
    }//GEN-LAST:event_jToggleButtonPauseActionPerformed

    private void jMenuItemOpenInBrowserActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemOpenInBrowserActionPerformed
    {//GEN-HEADEREND:event_jMenuItemOpenInBrowserActionPerformed
        if (jTableTreeResults.getSelectedRowCount() > 0)
        {
            try
            {
                
                ResultsNode node =(ResultsNode) ((TreeTableModelAdapter) jTableTreeResults.getModel()).getRowNode(jTableTreeResults.getSelectedRow());
                String tempURL = node.getResult().getItemFound().toString();
                System.out.println("URL from tree item: "+ tempURL);
                BrowserLauncher launcher = new BrowserLauncher(null);
                launcher.openURLinBrowser(tempURL);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jMenuItemOpenInBrowserActionPerformed

    private void jMenuItemViewResponseActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemViewResponseActionPerformed
    {//GEN-HEADEREND:event_jMenuItemViewResponseActionPerformed
        
        //TODO: re add this once we have completed the work how the results are stored

        ResultsNode node =(ResultsNode) ((TreeTableModelAdapter) jTableTreeResults.getModel()).getRowNode(jTableTreeResults.getSelectedRow());
        
        Result item = node.getResult();
        
        /*
        new JDialogViewResponce(parent, true,
                resultsTableModel.getRowResponce(select),
                resultsTableModel.getSelectedURL(select),
                resultsTableModel.getBaseCase(select),
                resultsTableModel.getRowRawResponce(select),
                resultsTableModel.getBaseCaseObj(select)).setVisible(true);
         */
        /*
        new JDialogViewResponse(parent, true,
                item.getFullURL(),
                item.getResponce(),
                item.getBaseCase(),
                item.getRawResponce(),
                item.getBaseCaseObj()).setVisible(true);
        */
}//GEN-LAST:event_jMenuItemViewResponseActionPerformed

    private void jMenuItemCopyURLActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemCopyURLActionPerformed
    {//GEN-HEADEREND:event_jMenuItemCopyURLActionPerformed
        ResultsNode node =(ResultsNode) ((TreeTableModelAdapter) jTableTreeResults.getModel()).getRowNode(jTableTreeResults.getSelectedRow());
        
        Result item = node.getResult();
        
        Clipboard clip = getToolkit().getSystemClipboard();

        StringSelection urlFromTable = new StringSelection(item.getItemFound().toString());
        clip.setContents(urlFromTable, this);
    }//GEN-LAST:event_jMenuItemCopyURLActionPerformed

    /*
     * Function to process the selection and deselection of extentions to be scanned
     * 
     */
    private synchronized void dymanicMenuAction(java.awt.event.ActionEvent evt)
    {
        JCheckBoxMenuItem item = (JCheckBoxMenuItem) evt.getSource();

        //remove the item from the work queue if it is there
        if (parent.manager.dirQueue.contains(localDirToCheck))
        {
            parent.manager.dirQueue.remove(localDirToCheck);
        }

        //change the toCheck object to reflect the change.
        Vector v = localDirToCheck.getExts();

        for (int a = 0; a < v.size(); a++)
        {
            ExtToCheck extToCheck = (ExtToCheck) v.elementAt(a);
            if (extToCheck.getName().equalsIgnoreCase(item.getText()))
            {
                //remove the elemente
                v.removeElementAt(a);
                //make the change
                extToCheck.setToCheck(item.isSelected());
                //read the element
                v.insertElementAt(extToCheck, a);
            }
        }

        //add the exts to be checked
        localDirToCheck.setExts(v);

        try
        {
            //re add the item to the queue
            //note this function will call a wait if the queue is full, might have to
            //move this process to a thread to to stop the GUI from hanging
            parent.manager.dirQueue.put(localDirToCheck);

        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }

        //clear the local cache
        localDirToCheck = null;

    }

    public void upDateResult(String finished, String started)
    {
        resultsTableModel.updateRow(finished, started);
    }

    public void setCurrentSpeed(String progress)
    {
        jLabelCurrentSpeed.setText(progress);
    }

    public void setAverageSpeed(String progress)
    {
        jLabelAverageSpeed.setText(progress);
    }

    public void setTotalRequests(String progress)
    {
        jLabelTotalRequests.setText(progress);
    }

    public void addScanInfoObject(JPanelScanInfo scanInfoPanel)
    {
        jPanelScanInfoBase.add(scanInfoPanel);

    }

    public void removeAllScanInfoObjects()
    {
        jPanelScanInfoBase.removeAll();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton jButtonBack;
    public javax.swing.JButton jButtonChangeThreads;
    public javax.swing.JButton jButtonReport;
    public javax.swing.JButton jButtonStop;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabelAverageSpeed;
    public javax.swing.JLabel jLabelCurrentSpeed;
    public javax.swing.JLabel jLabelCurrentTarget;
    public javax.swing.JLabel jLabelNumThreads;
    public javax.swing.JLabel jLabelParseQueueLength;
    public javax.swing.JLabel jLabelTimeLeft;
    public javax.swing.JLabel jLabelTotalRequests;
    public javax.swing.JMenuItem jMenuItemCopy;
    public javax.swing.JMenuItem jMenuItemCopyURL;
    public javax.swing.JMenuItem jMenuItemOpen;
    public javax.swing.JMenuItem jMenuItemOpenInBrowser;
    public javax.swing.JMenuItem jMenuItemView;
    public javax.swing.JMenuItem jMenuItemViewResponse;
    public javax.swing.JPanel jPanelScanInfoBase;
    public javax.swing.JPopupMenu jPopupMenuOpenBrowser;
    public javax.swing.JPopupMenu jPopupMenuTreeTableResults;
    public javax.swing.JScrollPane jScrollPaneErrorView;
    public javax.swing.JScrollPane jScrollPaneListResults;
    public javax.swing.JScrollPane jScrollPaneScanInformation;
    public javax.swing.JScrollPane jScrollPaneTreeView;
    public javax.swing.JTabbedPane jTabbedPaneViewResults;
    public javax.swing.JTable jTableErrors;
    public javax.swing.JTable jTableResults;
    public javax.swing.JTextField jTextFieldNumberOfThreads;
    public javax.swing.JToggleButton jToggleButtonPause;
    // End of variables declaration//GEN-END:variables
    public void setShowTarget(String target)
    {
        this.jLabelCurrentTarget.setText(target);
    }
    
    public int getSelectedItemInTable()
    {
        try
        {
            
            return jTableResults.getRowSorter().convertRowIndexToModel(jTableResults.getSelectedRow());
        }
        catch (java.lang.NoSuchMethodError e)
        {
            return jTableResults.getSelectedRow();
        }
    }
}

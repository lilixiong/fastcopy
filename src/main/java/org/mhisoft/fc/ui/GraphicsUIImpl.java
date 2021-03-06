/*
 * Copyright (c) 2014- MHISoft LLC and/or its affiliates. All rights reserved.
 * Licensed to MHISoft LLC under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. MHISoft LLC licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.mhisoft.fc.ui;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.mhisoft.fc.FastCopy;
import org.mhisoft.fc.FileCopyStatistics;

/**
 * Description:  Swing UI implementation.
 *
 * @author Tony Xue
 * @since Nov, 2014
 */
public class GraphicsUIImpl extends AbstractUIImpl {

	JTextArea outputTextArea;
	JLabel labelStatus;
	JProgressBar progressBar;

	public GraphicsUIImpl(JTextArea outputTextArea) {
		this.outputTextArea = outputTextArea;
	}

	public GraphicsUIImpl() {
	}

	public JTextArea getOutputTextArea() {
		return outputTextArea;
	}

	public void setOutputTextArea(JTextArea outputTextArea) {
		this.outputTextArea = outputTextArea;
	}

	public JLabel getLabelStatus() {
		return labelStatus;
	}

	public void setLabelStatus(JLabel labelStatus) {
		this.labelStatus = labelStatus;
	}

	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	@Override
	public void print(final String msg) {
		//invokeLater()
		//This method allows us to post a "job" to Swing, which it will then run
		// on the event dispatch thread at its next convenience.

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Here, we can safely update the GUI
				// because we'll be called from the
				// event dispatch thread
				outputTextArea.append(msg);
				outputTextArea.setCaretPosition(outputTextArea.getDocument().getLength());
				//labelStatus.setText(msg);
			}
		});

	}

	@Override
	public  void println(final String msg) {
		print(msg+"\n");
	}

	@Override
	public  void printf(final String msg, Object args) {
		//
	}

	@Override
	public  boolean isAnswerY(String question) {
		int dialogResult = JOptionPane.showConfirmDialog(null, question, "Please confirm", JOptionPane.YES_NO_OPTION);
		return dialogResult == JOptionPane.YES_OPTION;
	}

	@Override
	public Confirmation getConfirmation(String question, String... options) {
		int dialogResult = JOptionPane.showConfirmDialog(null, question, "Please confirm", JOptionPane.YES_NO_OPTION);
		if (JOptionPane.YES_OPTION==dialogResult) {
			return  Confirmation.YES;
		}
		else
			return  Confirmation.NO;

		//todo support presend a check box to check Yes for all future confirmations
		//return  Confirmation.YES_TO_ALL
	}

	@Override
	public void help() {
		printBuildAndDisclaimer();
	}

	@Override
	public FastCopy.RunTimeProperties parseCommandLineArguments(String[] args) {

		FastCopy.RunTimeProperties props= new FastCopy.RunTimeProperties();

		if (args.length<1 || args[0]==null || args[0].trim().length()==0) {
			//JOptionPane.showMessageDialog(null, "The root dir to start with can't be determined from args[].", "Error"
			//		, JOptionPane.ERROR_MESSAGE);
			//props.setSuccess(false);
			props.setSourceDir(null);
		}
		else {
			if (args.length>=1)
				props.setSourceDir(args[0]);
			if (args.length>=2)
				props.setDestDir(args[1]);
		}
		return props;
	}

	long lastProgressTime = -1;

	@Override
	public void reset() {
		lastProgressTime=-1;
		labelStatus.setText("");
		progressBar.setValue(0);
	}

	//0..100
	public void showProgress(int value, FileCopyStatistics statistics) {
		if (lastProgressTime==-1 || (System.currentTimeMillis()-lastProgressTime)>1000) {
			progressBar.setValue(value);
			labelStatus.setText( statistics.printOverallProgress());
			lastProgressTime = System.currentTimeMillis();
		}
	}
}

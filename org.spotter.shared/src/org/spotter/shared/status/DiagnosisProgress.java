/**
 * Copyright 2014 SAP AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.spotter.shared.status;

/**
 * Representation of the progress of a diagnosis step of Dynamic Spotter run.
 * 
 * @author Alexander Wert
 * 
 */
public class DiagnosisProgress {
	private String name;
	private DiagnosisStatus status;
	private double estimatedProgress;
	private long estimatedRemainingDuration; // [s]
	private String currentProgressMessage;

	/**
	 * Default Constructor required for serialization.
	 */
	public DiagnosisProgress() {
	}

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            problem name
	 * @param status
	 *            diagnosis status
	 * @param estimatedProgress
	 *            estimated progress in percent
	 * @param estimatedRemainingDuration
	 *            estimated remaining duration in seconds
	 * @param currentProgressMessage
	 *            additional progress message
	 */
	public DiagnosisProgress(String name, DiagnosisStatus status, double estimatedProgress,
			long estimatedRemainingDuration, String currentProgressMessage) {
		super();
		this.name = name;
		this.status = status;
		this.estimatedProgress = estimatedProgress;
		this.estimatedRemainingDuration = estimatedRemainingDuration;
		this.currentProgressMessage = currentProgressMessage;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the status
	 */
	public synchronized DiagnosisStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public synchronized void setStatus(DiagnosisStatus status) {
		this.status = status;
	}

	/**
	 * @return the estimatedProgress
	 */
	public synchronized double getEstimatedProgress() {
		return estimatedProgress;
	}

	/**
	 * @param estimatedProgress
	 *            the estimatedProgress to set
	 */
	public synchronized void setEstimatedProgress(double estimatedProgress) {
		this.estimatedProgress = estimatedProgress;
	}

	/**
	 * @return the estimatedRemainingDuration
	 */
	public synchronized long getEstimatedRemainingDuration() {
		return estimatedRemainingDuration;
	}

	/**
	 * @param estimatedRemainingDuration
	 *            the estimatedRemainingDuration to set
	 */
	public synchronized void setEstimatedRemainingDuration(long estimatedRemainingDuration) {
		this.estimatedRemainingDuration = estimatedRemainingDuration;
	}

	/**
	 * @return the currentProgressMessage
	 */
	public synchronized String getCurrentProgressMessage() {
		return currentProgressMessage;
	}

	/**
	 * @param currentProgressMessage
	 *            the currentProgressMessage to set
	 */
	public synchronized void setCurrentProgressMessage(String currentProgressMessage) {
		this.currentProgressMessage = currentProgressMessage;
	}

}

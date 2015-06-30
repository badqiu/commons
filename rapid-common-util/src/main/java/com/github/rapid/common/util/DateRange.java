package com.github.rapid.common.util;

import java.util.Date;

/**
 * 日期范围工具类
 * 
 * @author badqiu
 **/
public class DateRange {

	private Date startDate;
	private Date endDate;

	/**
	 * Construct a new DateRange with null values for the start and end date.
	 */
	public DateRange() {
	}

	/**
	 * Construct a new DateRanger with the given start and end date.
	 * 
	 * @param startDate
	 *            The start date
	 * @param endDate
	 *            The end date
	 */

	public DateRange(Date startDate, Date endDate) {
		setStartDate(startDate);
		setEndDate(endDate);
	}

	/**
	 * Get the start date. This value may be null.
	 * 
	 * @return The start date
	 */

	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Set the start date. This value may be null.
	 * 
	 * @param startDate
	 *            The new start date
	 */

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Get the end date. This value may be null.
	 * 
	 * @return The end date
	 */

	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Set the end date. This value may be null.
	 * 
	 * @param endDate
	 *            The new end date
	 */

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Return true if the given Date is after the start date and before the end
	 * date. If either date is null then that part of the test will
	 * automatically be true.
	 * 
	 * @param date
	 *            The Date to test
	 * @return true if the Date is between the start and end date
	 */

	public boolean isWithinRange(Date date) {
		if(date == null) return false;
		if (startDate != null && date.after(startDate)) {
			if (endDate != null && date.before(endDate)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return the number of segments in the range given that a segment is
	 * <code>segmentLength</code> milliseconds.
	 * 
	 * @param segmentLength
	 *            Segment length in milliseconds
	 * @return The number of segments
	 * @throws IllegalStateException
	 *             If either the start or end date are not set
	 */

	public double getSegmentCount(int segmentLength)
			throws IllegalStateException {
		if (startDate == null || endDate == null) {
			throw new IllegalStateException("Operation cannot be performed");
		}

		long startTime = startDate.getTime();
		long endTime = endDate.getTime();

		return (endTime - startTime) / segmentLength;
	}

	/**
	 * Return a String representation of the DateRange.
	 * 
	 * @return A String representing the date range
	 */

	public String toString() {
		return "[" + startDate + " to " + endDate + "]";
	}

}
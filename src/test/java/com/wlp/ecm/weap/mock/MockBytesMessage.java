package com.wlp.ecm.weap.mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MessageEOFException;
import javax.jms.MessageFormatException;
import javax.jms.MessageNotReadableException;
import javax.jms.MessageNotWriteableException;

public class MockBytesMessage extends MockMessage implements BytesMessage {
	private DataOutputStream outStream;
	private ByteArrayOutputStream byteOutStream;
	private DataInputStream inStream;

	public MockBytesMessage() {
		try {
			clearBody();
		} catch (JMSException exc) {
			throw new MockMessageException(exc);
		}
	}

	public MockBytesMessage(String readFilePath) {
		FileInputStream fis = null;

		try {
			//clearBody();
			
			File f = new File(readFilePath);
			byte[] bytes = new byte[(int)f.length()];
			
			fis = new FileInputStream(readFilePath);
			fis.read(bytes);
			
			byteOutStream = new ByteArrayOutputStream();
			byteOutStream.write(bytes);
			outStream = new DataOutputStream(byteOutStream);
		
		} catch (FileNotFoundException ex2) {
			throw new MockMessageException(ex2);
		} catch (IOException ex3) {
			throw new MockMessageException(ex3);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					throw new MockMessageException(e);
				}
			}
		}
	}

	@Override
	public long getBodyLength() throws JMSException {
		if (isInWriteMode()) {
			throw new MessageNotReadableException("Message is in write mode");
		}
		//return outStream.size();
		int available = 0;
		try {
			available = inStream.available();
		} catch (IOException e) {
			throw new JMSException(e.getMessage());
		}
		return available;
	}

	@Override
	public boolean readBoolean() throws JMSException {
		if (isInWriteMode()) {
			throw new MessageNotReadableException("Message is in write mode");
		}
		try {
			return inStream.readBoolean();
		} catch (EOFException exc) {
			throw new MessageEOFException(exc.getMessage());
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
	}

	@Override
	public byte readByte() throws JMSException {
		if (isInWriteMode()) {
			throw new MessageNotReadableException("Message is in write mode");
		}
		try {
			return inStream.readByte();
		} catch (EOFException exc) {
			throw new MessageEOFException(exc.getMessage());
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
	}

	@Override
	public int readUnsignedByte() throws JMSException {
		if (isInWriteMode()) {
			throw new MessageNotReadableException("Message is in write mode");
		}
		try {
			return inStream.readByte();
		} catch (EOFException exc) {
			throw new MessageEOFException(exc.getMessage());
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
	}

	@Override
	public short readShort() throws JMSException {
		if (isInWriteMode()) {
			throw new MessageNotReadableException("Message is in write mode");
		}
		try {
			return inStream.readShort();
		} catch (EOFException exc) {
			throw new MessageEOFException(exc.getMessage());
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
	}

	@Override
	public int readUnsignedShort() throws JMSException {
		if (isInWriteMode()) {
			throw new MessageNotReadableException("Message is in write mode");
		}
		try {
			return inStream.readShort();
		} catch (EOFException exc) {
			throw new MessageEOFException(exc.getMessage());
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
	}

	@Override
	public char readChar() throws JMSException {
		if (isInWriteMode()) {
			throw new MessageNotReadableException("Message is in write mode");
		}
		try {
			return inStream.readChar();
		} catch (EOFException exc) {
			throw new MessageEOFException(exc.getMessage());
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
	}

	@Override
	public int readInt() throws JMSException {
		if (isInWriteMode()) {
			throw new MessageNotReadableException("Message is in write mode");
		}
		try {
			return inStream.readInt();
		} catch (EOFException exc) {
			throw new MessageEOFException(exc.getMessage());
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
	}

	@Override
	public long readLong() throws JMSException {
		if (isInWriteMode()) {
			throw new MessageNotReadableException("Message is in write mode");
		}
		try {
			return inStream.readLong();
		} catch (EOFException exc) {
			throw new MessageEOFException(exc.getMessage());
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
	}

	@Override
	public float readFloat() throws JMSException {
		if (isInWriteMode()) {
			throw new MessageNotReadableException("Message is in write mode");
		}
		try {
			return inStream.readFloat();
		} catch (EOFException exc) {
			throw new MessageEOFException(exc.getMessage());
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
	}

	@Override
	public double readDouble() throws JMSException {
		if (isInWriteMode()) {
			throw new MessageNotReadableException("Message is in write mode");
		}
		try {
			return inStream.readDouble();
		} catch (EOFException exc) {
			throw new MessageEOFException(exc.getMessage());
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
	}

	@Override
	public String readUTF() throws JMSException {
		if (isInWriteMode()) {
			throw new MessageNotReadableException("Message is in write mode");
		}
		try {
			return inStream.readUTF();
		} catch (EOFException exc) {
			throw new MessageEOFException(exc.getMessage());
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
	}

	@Override
	public int readBytes(byte[] data) throws JMSException {
		if (isInWriteMode()) {
			throw new MessageNotReadableException("Message is in write mode");
		}
		try {
			return inStream.read(data);
		} catch (EOFException exc) {
			throw new MessageEOFException(exc.getMessage());
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
	}

	@Override
	public int readBytes(byte[] data, int length) throws JMSException {
		if (isInWriteMode()) {
			throw new MessageNotReadableException("Message is in write mode");
		}
		try {
			return inStream.read(data, 0, length);
		} catch (EOFException exc) {
			throw new MessageEOFException(exc.getMessage());
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
	}

	@Override
	public void writeBoolean(boolean value) throws JMSException {
		if (!isInWriteMode()) {
			throw new MessageNotWriteableException("Message is in read mode");
		}
		try {
			outStream.writeBoolean(value);
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
	}

	@Override
	public void writeByte(byte value) throws JMSException {
		if (!isInWriteMode()) {
			throw new MessageNotWriteableException("Message is in read mode");
		}
		try {
			outStream.writeByte(value);
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
	}

	@Override
	public void writeShort(short value) throws JMSException {
		if (!isInWriteMode()) {
			throw new MessageNotWriteableException("Message is in read mode");
		}
		try {
			outStream.writeShort(value);
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
	}

	@Override
	public void writeChar(char value) throws JMSException {
		if (!isInWriteMode()) {
			throw new MessageNotWriteableException("Message is in read mode");
		}
		try {
			outStream.writeChar(value);
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
	}

	@Override
	public void writeInt(int value) throws JMSException {
		if (!isInWriteMode()) {
			throw new MessageNotWriteableException("Message is in read mode");
		}
		try {
			outStream.writeInt(value);
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
	}

	@Override
	public void writeLong(long value) throws JMSException {
		if (!isInWriteMode()) {
			throw new MessageNotWriteableException("Message is in read mode");
		}
		try {
			outStream.writeLong(value);
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
	}

	@Override
	public void writeFloat(float value) throws JMSException {
		if (!isInWriteMode()) {
			throw new MessageNotWriteableException("Message is in read mode");
		}
		try {
			outStream.writeFloat(value);
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
	}

	@Override
	public void writeDouble(double value) throws JMSException {
		if (!isInWriteMode()) {
			throw new MessageNotWriteableException("Message is in read mode");
		}
		try {
			outStream.writeDouble(value);
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
	}

	@Override
	public void writeUTF(String value) throws JMSException {
		if (!isInWriteMode()) {
			throw new MessageNotWriteableException("Message is in read mode");
		}
		try {
			outStream.writeUTF(value);
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
	}

	@Override
	public void writeBytes(byte[] data) throws JMSException {
		if (!isInWriteMode()) {
			throw new MessageNotWriteableException("Message is in read mode");
		}
		try {
			outStream.write(data);
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
	}

	@Override
	public void writeBytes(byte[] data, int offset, int length)
			throws JMSException {
		if (!isInWriteMode()) {
			throw new MessageNotWriteableException("Message is in read mode");
		}
		try {
			outStream.write(data, offset, length);
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
	}

	@Override
	public void writeObject(Object object) throws JMSException {
		if (!isInWriteMode()) {
			throw new MessageNotWriteableException("Message is in read mode");
		}
		if (object instanceof Byte) {
			writeByte(((Byte) object).byteValue());
			return;
		}
		if (object instanceof Short) {
			writeShort(((Short) object).shortValue());
			return;
		}
		if (object instanceof Integer) {
			writeInt(((Integer) object).intValue());
			return;
		}
		if (object instanceof Long) {
			writeLong(((Long) object).longValue());
			return;
		}
		if (object instanceof Float) {
			writeFloat(((Float) object).floatValue());
			return;
		}
		if (object instanceof Double) {
			writeDouble(((Double) object).doubleValue());
			return;
		}
		if (object instanceof Character) {
			writeChar(((Character) object).charValue());
			return;
		}
		if (object instanceof Boolean) {
			writeBoolean(((Boolean) object).booleanValue());
			return;
		}
		if (object instanceof String) {
			writeUTF((String) object);
			return;
		}
		if (object instanceof byte[]) {
			writeBytes((byte[]) object);
			return;
		}
		throw new MessageFormatException(object.getClass().getName()
				+ " is an invalid type");
	}

	@Override
	public void reset() throws JMSException {
		setReadOnly(true);
		try {
			outStream.flush();
		} catch (IOException exc) {
			throw new JMSException(exc.getMessage());
		}
		inStream = new DataInputStream(new ByteArrayInputStream(
				byteOutStream.toByteArray()));
	}

	@Override
	public void clearBody() throws JMSException {
		super.clearBody();
		byteOutStream = new ByteArrayOutputStream();
		outStream = new DataOutputStream(byteOutStream);
	}

	/**
	 * Returns a copy of the underlying byte data regardless if the message is
	 * in read or write mode.
	 * 
	 * @return the byte data
	 */
	public byte[] getBytes() {
		try {
			outStream.flush();
		} catch (IOException exc) {
			throw new RuntimeException(exc.getMessage());
		}
		return byteOutStream.toByteArray();
	}

	/**
	 * Compares the underlying byte data.
	 */
	@Override
	public boolean equals(Object otherObject) {
		if (null == otherObject)
			return false;
		if (!(otherObject instanceof MockBytesMessage))
			return false;
		MockBytesMessage otherMessage = (MockBytesMessage) otherObject;
		byte[] firstData = getBytes();
		byte[] secondData = otherMessage.getBytes();
		return Arrays.equals(firstData, secondData);
	}

	@Override
	public int hashCode() {
		int value = 17;
		byte[] data = getBytes();
		for (int ii = 0; ii < data.length; ii++) {
			value = (31 * value) + data[ii];
		}
		return value;
	}

	@Override
	public Object clone() {
		MockBytesMessage message = (MockBytesMessage) super.clone();
		try {
			message.clearBody();
			message.outStream.write(getBytes());
			return message;
		} catch (Exception exc) {
			throw new MockMessageException(exc);
		}
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.getClass().getName() + ": [");
		byte[] data = getBytes();
		for (int ii = 0; ii < data.length; ii++) {
			buffer.append(data[ii]);
			if (ii < data.length - 1) {
				buffer.append(", ");
			}
		}
		buffer.append("]");
		return buffer.toString();
	}

}

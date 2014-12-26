package com.rafael.sdk.util;

import java.nio.ByteBuffer;

import com.sun.corba.se.impl.io.TypeMismatchException;
import com.sun.org.apache.bcel.internal.generic.Type;

public final class Bundle {

	private static Randomizer randomizer = new Randomizer(Short.MAX_VALUE);
	private String topic = null;
	private String destination = null;
	ByteBuffer byteBuffer = null;
	private boolean pureData;
	public static int MAX_BUNDLE_SIZE = 10000;
	private static byte BYTE_ARRAY_TYPE = 20;
    
	private Bundle() {
		pureData = false;
	}
	
	public Bundle(String topic) {
		this(topic, MAX_BUNDLE_SIZE);
    }
	
	public Bundle(String topic, int bundleSize) {
		this(topic, ByteBuffer.allocate(bundleSize));
	}
	
	public Bundle(String topic, ByteBuffer byteBuffer) {
		pureData = false;
        setTopic(topic);
        this.byteBuffer = byteBuffer;
        prepare();
    }
	
	public Bundle(Bundle bundle) {
		pureData = bundle.pureData;
		byteBuffer = bundle.byteBuffer.duplicate();		
		topic = bundle.topic;
	}
	
    private void setRequestId(int requestId) {
		byteBuffer.putInt(4 + getTopicSize(), requestId);
    }
    
    private int getRequestId() {
    	return byteBuffer.getInt(4 + getTopicSize());
    }
    
    private int getTopicSize() {
    	return byteBuffer.getInt(0);
    }
    
	public static byte[] serialize(Bundle bundle) {
		return bundle.byteBuffer.array();
	}
	
	public static Bundle deserialize(byte[] data, int size) {
		Bundle bundle = Bundle.createBundle(data, size);
		return bundle;
	}

    public int generateRequestId() {
    	int requestId = nextRequestId();
    	setRequestId(requestId);
    	return requestId;
    }
    
    private synchronized int nextRequestId() {
    	int requestId = randomizer.nextBoundedNonRepeatInt();
    	return requestId;
    }
    
    public String getTopicWithRequestId() {
    	return (getRequestId() + "_" + getTopic());
    }
    
    public void updateReplyTopic() {
    	setTopic(getTopicWithRequestId());
    }
    
    /**
     * Get the topic
     * @return the topic for the bundle
     */
    public String getTopic() {
    	return topic;
	}
    
    public int size() {
    	return byteBuffer.limit();
    }

    /**
     * Set the topic for the bundle
     * @param inTopic the topic for the bundle
     */
	public void setTopic(String topic) {
		this.topic = topic;
	}

    public void clear() {
    	byteBuffer.clear();
    	prepare();
    }

    
    private void prepare() {
    	byteBuffer.position(0);
        // store the topic size
        byteBuffer.putInt(topic.length());
        // store the topic
        byteBuffer.put(topic.getBytes());        
        // store the requestId
        byteBuffer.putInt(0);
        // set current size
        byteBuffer.limit(8 + getTopicSize());
    }
    
    public void markField() {
    	byteBuffer.mark();
    }
    
    public void gotoMarkedField() {
    	byteBuffer.reset();
    }
    
    public void gotoFirstField() {
    	byteBuffer.position(8 + getTopicSize());
    }
    
    /**
     * Inserts a Boolean value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key a String, or null
     * @param value a Boolean, or null
     */
    public boolean putBoolean(String key, boolean value) {
        if (false == beforePut(2, Type.BOOLEAN.getType())) {
        	return false;
        }
    	byte b = (value)? (byte)1:0;
        byteBuffer.put(b);
        afterPut();
        return true;
    }

    /**
     * Inserts a byte value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key a String, or null
     * @param value a byte
     */
    public boolean putByte(String key, byte value) {
        if (false == beforePut(2, Type.BYTE.getType())) {
        	return false;
        }
    	byteBuffer.put(value);
    	afterPut();
        return true;
    }

    /**
     * Inserts a char value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key a String, or null
     * @param value a char, or null
     */
    public boolean putChar(String key, char value) {
        if (false == beforePut(3, Type.CHAR.getType())) {
        	return false;
        }
        byteBuffer.putChar(value);
    	afterPut();
        return true;
    }

    /**
     * Inserts a short value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key a String, or null
     * @param value a short
     */
    public boolean putShort(String key, short value) {
        if (false == beforePut(3, Type.SHORT.getType())) {
        	return false;
        }
        byteBuffer.putShort(value);
        afterPut();
        return true;
    }

    /**
     * Inserts an int value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key a String, or null
     * @param value an int, or null
     */
    public boolean putInt(String key, int value) {
        if (false == beforePut(5, Type.INT.getType())) {
        	return false;
        }
        byteBuffer.putInt(value);
        afterPut();
        return true;
    }

    /**
     * Inserts a long value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key a String, or null
     * @param value a long
     */
    public boolean putLong(String key, long value) {
        if (false == beforePut(9, Type.LONG.getType())) {
        	return false;
        }
    	byteBuffer.putLong(value);
    	afterPut();
    	return true;
    }

    /**
     * Inserts a float value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key a String, or null
     * @param value a float
     */
    public boolean putFloat(String key, float value) {
        if (false == beforePut(5, Type.FLOAT.getType())) {
        	return false;
        }
    	byteBuffer.putFloat(value);
    	afterPut();
    	return true;
    }

    /**
     * Inserts a double value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key a String, or null
     * @param value a double
     */
    public boolean putDouble(String key, double value) {
        if (false == beforePut(9, Type.DOUBLE.getType())) {
        	return false;
        }
    	byteBuffer.putDouble(value);
    	afterPut();
    	return true;
    }

    /**
     * Inserts a String value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key a String, or null
     * @param value a String, or null
     */
    public boolean putString(String key, String value) {
        if (false == beforePut(value.length()+5, Type.STRING.getType())) {
        	return false;
        }
    	byteBuffer.putInt(value.length());
        byteBuffer.put(value.getBytes());
        afterPut();
        return true;
    }

    /**
     * Inserts a byte array value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key a String, or null
     * @param value a byte array object, or null
     */
    public boolean putByteArray(String key, byte[] value) {
        if (false == beforePut(value.length+5, BYTE_ARRAY_TYPE)) {
        	return false;
        }
    	byteBuffer.putInt(value.length);
    	byteBuffer.put(value);
    	afterPut();
    	return true;
    }
    
    public boolean putBundle(Bundle bundle) {
    	topic = bundle.topic;
    	pureData = bundle.pureData;
    	byteBuffer = bundle.byteBuffer;
    	return true;
    }

    public static Bundle createBundle(byte[] data, int size) {
    	Bundle bundle = new Bundle();
    	bundle.byteBuffer = ByteBuffer.wrap(data);
    	bundle.topic = getStoredTopic(bundle.byteBuffer);
    	bundle.byteBuffer.position(8+bundle.getTopicSize());
		bundle.byteBuffer.limit(size);
		bundle.pureData = true;
    	return bundle;
    }
    
    public static Bundle createBundle(String topic, byte[] data, int size) {
    	Bundle bundle = new Bundle();
    	bundle.byteBuffer = ByteBuffer.wrap(data);
    	bundle.topic = topic;
    	bundle.byteBuffer.position(0);
		bundle.byteBuffer.limit(size);
		bundle.pureData = true;
    	return bundle;
    }

    private static String getStoredTopic(ByteBuffer byteBuffer) {
		byteBuffer.position(0);
    	int topicSize = byteBuffer.getInt();
		byte[] topicBytes = new byte[topicSize];
		byteBuffer.get(topicBytes, 0, topicSize);
		return new String(topicBytes);
    }
    
    public byte getNextFieldType() {
    	byte type = byteBuffer.get();
    	byteBuffer.position(byteBuffer.position()-1);
    	return type;    	
    }
    
    /**
     * Returns the value associated with the given key, or false if
     * no mapping of the desired type exists for the given key.
     *
     * @param key a String
     * @return a boolean value
     */
    public boolean getBoolean(String key) {
        if (Type.BOOLEAN.getType() != getNextFieldType()) {
        	throw new TypeMismatchException("Can't convert next field to boolean");
        }
        byteBuffer.get();
    	return (byteBuffer.get() == 1);
    }

    /**
     * Returns the value associated with the given key, or (byte) 0 if
     * no mapping of the desired type exists for the given key.
     *
     * @param key a String
     * @return a byte value
     */
    public byte getByte(String key) {
        if (Type.BYTE.getType() != getNextFieldType()) {
        	throw new TypeMismatchException("Can't convert next field to byte");
        }
        byteBuffer.get();
        return byteBuffer.get();
    }
    
    public byte[] getByteArray(String key) {
        if (BYTE_ARRAY_TYPE != getNextFieldType()) {
        	throw new TypeMismatchException("Can't convert next field to byte array");
        }
        byteBuffer.get();
    	int length = byteBuffer.getInt();
    	byte[] bytes = new byte[length];
    	byteBuffer.get(bytes, 0, length);
    	return bytes;
    }

    /**
     * Returns the value associated with the given key, or false if
     * no mapping of the desired type exists for the given key.
     *
     * @param key a String
     * @return a char value
     */
    public char getChar(String key) {
        if (Type.CHAR.getType() != getNextFieldType()) {
        	throw new TypeMismatchException("Can't convert next field to char");
        }
        byteBuffer.get();
        return byteBuffer.getChar();
    }

    /**
     * Returns the value associated with the given key, or (short) 0 if
     * no mapping of the desired type exists for the given key.
     *
     * @param key a String
     * @return a short value
     */
    public short getShort(String key) {
        if (Type.SHORT.getType() != getNextFieldType()) {
        	throw new TypeMismatchException("Can't convert next field to short");
        }
        byteBuffer.get();
        return byteBuffer.getShort();
    }

    /**
     * Returns the value associated with the given key, or 0 if
     * no mapping of the desired type exists for the given key.
     *
     * @param key a String
     * @return an int value
     */
    public int getInt(String key) {
        if (Type.INT.getType() != getNextFieldType()) {
        	throw new TypeMismatchException("Can't convert next field to int");
        }
        byteBuffer.get();
        return byteBuffer.getInt();
    }

    /**
     * Returns the value associated with the given key, or 0L if
     * no mapping of the desired type exists for the given key.
     *
     * @param key a String
     * @return a long value
     */
    public long getLong(String key) {
        if (Type.LONG.getType() != getNextFieldType()) {
        	throw new TypeMismatchException("Can't convert next field to long");
        }
        byteBuffer.get();
        return byteBuffer.getLong();
    }

    /**
     * Returns the value associated with the given key, or 0.0f if
     * no mapping of the desired type exists for the given key.
     *
     * @param key a String
     * @return a float value
     */
    public float getFloat(String key) {
        if (Type.FLOAT.getType() != getNextFieldType()) {
        	throw new TypeMismatchException("Can't convert next field to float");
        }
        byteBuffer.get();
        return byteBuffer.getFloat();
    }

    /**
     * Returns the value associated with the given key, or 0.0 if
     * no mapping of the desired type exists for the given key.
     *
     * @param key a String
     * @return a double value
     */
    public double getDouble(String key) {
        if (Type.DOUBLE.getType() != getNextFieldType()) {
        	throw new TypeMismatchException("Can't convert next field to double");
        }
        byteBuffer.get();
        return byteBuffer.getDouble();
    }

    /**
     * Returns the value associated with the given key, or null if
     * no mapping of the desired type exists for the given key or a null
     * value is explicitly associated with the key.
     *
     * @param key a String, or null
     * @return a String value, or null
     */
    public String getString(String key) {
        if (Type.STRING.getType() != getNextFieldType()) {
        	throw new TypeMismatchException("Can't convert next field to String");
        }
        byteBuffer.get();
    	int length = byteBuffer.getInt();
    	byte[] bytes = new byte[length];
    	byteBuffer.get(bytes, 0, length);
    	return new String(bytes);
    }
    
    public byte[] getData() throws NullPointerException {
    	if (pureData) {
    		return byteBuffer.array();
    	}
    	
    	throw new NullPointerException();
    }
    
    private boolean beforePut(int sizeof, byte type) {
        if ((size() + sizeof) > byteBuffer.capacity()) {
        	return false;
        }
    	byteBuffer.mark();
    	byteBuffer.position(size());
        byteBuffer.limit(size() + sizeof);
        byteBuffer.put(type);
        return true;
    }

    private void afterPut() {
    	byteBuffer.reset();
    }

    /**
     * Set the destination for the request
     * @param destination - The full class name containing the destination of the request.
     */
    public void setDestination(String destination){
    	this.destination = destination;
    }
    
    /**
     * This method returns the destination for the request
     * 
     * @return The destination of the request
     * @exception NullPointerException If no destination was specified.
     */
	public String getDestination(){
		if (null == destination){
			throw new NullPointerException("No destination specified for this request. Call 'setDestination' method to specify the destination of the request.");
		}
		return destination;
	}
	
	public boolean hasRemaining() {
		return byteBuffer.hasRemaining();
	}
}
package com.rafael.sdk.util;

import java.nio.ByteBuffer;

import com.sun.corba.se.impl.io.TypeMismatchException;
import com.sun.org.apache.bcel.internal.generic.Type;

// TODO: Auto-generated Javadoc
/**
 * The Class Bundle.
 */
public final class Bundle {

	/** The randomizer. */
	private static Randomizer randomizer = new Randomizer(Short.MAX_VALUE);
	
	/** The topic. */
	private String topic = null;
	
	/** The destination. */
	private String destination = null;
	
	/** The byte buffer. */
	ByteBuffer byteBuffer = null;
	
	/** The pure data. */
	private boolean pureData;
	
	/** The max bundle size. */
	public static int MAX_BUNDLE_SIZE = 10000;
	
	/** The byte array type. */
	private static byte BYTE_ARRAY_TYPE = 20;
    
	/**
	 * Instantiates a new bundle.
	 */
	private Bundle() {
		pureData = false;
	}
	
	/**
	 * Instantiates a new bundle.
	 *
	 * @param topic the topic
	 */
	public Bundle(String topic) {
		this(topic, MAX_BUNDLE_SIZE);
    }
	
	/**
	 * Instantiates a new bundle.
	 *
	 * @param topic the topic
	 * @param bundleSize the bundle size
	 */
	public Bundle(String topic, int bundleSize) {
		this(topic, ByteBuffer.allocate(bundleSize));
	}
	
	/**
	 * Instantiates a new bundle.
	 *
	 * @param topic the topic
	 * @param byteBuffer the byte buffer
	 */
	public Bundle(String topic, ByteBuffer byteBuffer) {
		pureData = false;
        setTopic(topic);
        this.byteBuffer = byteBuffer;
        prepare();
    }
	
	/**
	 * Instantiates a new bundle.
	 *
	 * @param bundle the bundle
	 */
	public Bundle(Bundle bundle) {
		pureData = bundle.pureData;
		byteBuffer = bundle.byteBuffer.duplicate();		
		topic = bundle.topic;
	}
	
    /**
     * Sets the request id.
     *
     * @param requestId the new request id
     */
    private void setRequestId(int requestId) {
		byteBuffer.putInt(4 + getTopicSize(), requestId);
    }
    
    /**
     * Gets the request id.
     *
     * @return the request id
     */
    private int getRequestId() {
    	return byteBuffer.getInt(4 + getTopicSize());
    }
    
    /**
     * Gets the topic size.
     *
     * @return the topic size
     */
    private int getTopicSize() {
    	return byteBuffer.getInt(0);
    }
    
	/**
	 * Serialize.
	 *
	 * @param bundle the bundle
	 * @return the byte[]
	 */
	public static byte[] serialize(Bundle bundle) {
		return bundle.byteBuffer.array();
	}
	
	/**
	 * Deserialize.
	 *
	 * @param data the data
	 * @param size the size
	 * @return the bundle
	 */
	public static Bundle deserialize(byte[] data, int size) {
		Bundle bundle = Bundle.createBundle(data, size);
		return bundle;
	}

    /**
     * Generate request id.
     *
     * @return the int
     */
    public int generateRequestId() {
    	int requestId = nextRequestId();
    	setRequestId(requestId);
    	return requestId;
    }
    
    /**
     * Next request id.
     *
     * @return the int
     */
    private synchronized int nextRequestId() {
    	int requestId = randomizer.nextBoundedNonRepeatInt();
    	return requestId;
    }
    
    /**
     * Gets the topic with request id.
     *
     * @return the topic with request id
     */
    public String getTopicWithRequestId() {
    	return (getRequestId() + "_" + getTopic());
    }
    
    /**
     * Update reply topic.
     */
    public void updateReplyTopic() {
    	setTopic(getTopicWithRequestId());
    }
    
    /**
     * Get the topic.
     *
     * @return the topic for the bundle
     */
    public String getTopic() {
    	return topic;
	}
    
    /**
     * Size.
     *
     * @return the int
     */
    public int size() {
    	return byteBuffer.limit();
    }

    /**
     * Set the topic for the bundle.
     *
     * @param topic the new topic
     */
	public void setTopic(String topic) {
		this.topic = topic;
	}

    /**
     * Clear.
     */
    public void clear() {
    	byteBuffer.clear();
    	prepare();
    }

    
    /**
     * Prepare.
     */
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
    
    /**
     * Mark field.
     */
    public void markField() {
    	byteBuffer.mark();
    }
    
    /**
     * Goto marked field.
     */
    public void gotoMarkedField() {
    	byteBuffer.reset();
    }
    
    /**
     * Goto first field.
     */
    public void gotoFirstField() {
    	byteBuffer.position(8 + getTopicSize());
    }
    
    /**
     * Inserts a Boolean value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key a String, or null
     * @param value a Boolean, or null
     * @return true, if successful
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
     * @return true, if successful
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
     * @return true, if successful
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
     * @return true, if successful
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
     * @return true, if successful
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
     * @return true, if successful
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
     * @return true, if successful
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
     * @return true, if successful
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
     * @return true, if successful
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
     * @return true, if successful
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
    
    /**
     * Put bundle.
     *
     * @param bundle the bundle
     * @return true, if successful
     */
    public boolean putBundle(Bundle bundle) {
    	topic = bundle.topic;
    	pureData = bundle.pureData;
    	byteBuffer = bundle.byteBuffer;
    	return true;
    }

    /**
     * Creates the bundle.
     *
     * @param data the data
     * @param size the size
     * @return the bundle
     */
    public static Bundle createBundle(byte[] data, int size) {
    	Bundle bundle = new Bundle();
    	bundle.byteBuffer = ByteBuffer.wrap(data);
    	bundle.topic = getStoredTopic(bundle.byteBuffer);
    	bundle.byteBuffer.position(8+bundle.getTopicSize());
		bundle.byteBuffer.limit(size);
		bundle.pureData = true;
    	return bundle;
    }
    
    /**
     * Creates the bundle.
     *
     * @param topic the topic
     * @param data the data
     * @param size the size
     * @return the bundle
     */
    public static Bundle createBundle(String topic, byte[] data, int size) {
    	Bundle bundle = new Bundle();
    	bundle.byteBuffer = ByteBuffer.wrap(data);
    	bundle.topic = topic;
    	bundle.byteBuffer.position(0);
		bundle.byteBuffer.limit(size);
		bundle.pureData = true;
    	return bundle;
    }

    /**
     * Gets the stored topic.
     *
     * @param byteBuffer the byte buffer
     * @return the stored topic
     */
    private static String getStoredTopic(ByteBuffer byteBuffer) {
		byteBuffer.position(0);
    	int topicSize = byteBuffer.getInt();
		byte[] topicBytes = new byte[topicSize];
		byteBuffer.get(topicBytes, 0, topicSize);
		return new String(topicBytes);
    }
    
    /**
     * Gets the next field type.
     *
     * @return the next field type
     */
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
    
    /**
     * Gets the byte array.
     *
     * @param key the key
     * @return the byte array
     */
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
    
    /**
     * Gets the data.
     *
     * @return the data
     * @throws NullPointerException the null pointer exception
     */
    public byte[] getData() throws NullPointerException {
    	if (pureData) {
    		return byteBuffer.array();
    	}
    	
    	throw new NullPointerException();
    }
    
    /**
     * Before put.
     *
     * @param sizeof the sizeof
     * @param type the type
     * @return true, if successful
     */
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

    /**
     * After put.
     */
    private void afterPut() {
    	byteBuffer.reset();
    }

    /**
     * Set the destination for the request.
     *
     * @param destination - The full class name containing the destination of the request.
     */
    public void setDestination(String destination){
    	this.destination = destination;
    }
    
    /**
     * This method returns the destination for the request.
     *
     * @return The destination of the request
     */
	public String getDestination(){
		if (null == destination){
			throw new NullPointerException("No destination specified for this request. Call 'setDestination' method to specify the destination of the request.");
		}
		return destination;
	}
	
	/**
	 * Checks for remaining.
	 *
	 * @return true, if successful
	 */
	public boolean hasRemaining() {
		return byteBuffer.hasRemaining();
	}
}
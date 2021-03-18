package net.kunmc.lab.cryptofthenecrodancer.nbs;

import java.io.*;
import java.util.HashMap;

public class Music {
    private final byte version;
    private final short length;
    private final short layerCount;
    private final String title;
    private final String author;
    private final String originalAuthor;
    private final String description;
    private final float tempo;
    private final byte timeSignature;
    private final boolean enableLoop;
    private final byte maxLoopCount;
    private final short loopStartTick;
    private final HashMap<Integer, Layer>  layers = new HashMap<>();
    private final boolean isStereo;

    public byte getVersion() {
        return version;
    }

    public short getLength() {
        return length;
    }

    public short getLayerCount() {
        return layerCount;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getOriginalAuthor() {
        return originalAuthor;
    }

    public String getDescription() {
        return description;
    }

    public float getTempo() {
        return tempo;
    }

    public byte getTimeSignature() {
        return timeSignature;
    }

    public HashMap<Integer, Layer> getLayers() { return layers; }

    public boolean isStereo() {
        return isStereo;
    }

    public Music(InputStream inputStream) {
        byte version = 0;
        short length = 0;
        short layerCount = 0;
        String title = "";
        String author = "";
        String originalAuthor = "";
        String description = "";
        float tempo = 0.0f;
        byte timeSignature = 0;
        boolean enableLoop = false;
        byte maxLoopCount = 0;
        short loopStartTick = 0;
        boolean isStereo = false;

        try {
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            length = readShort(dataInputStream);
            byte customInstrument = 10;
            if (length == 0) {
                version = dataInputStream.readByte();
                customInstrument = dataInputStream.readByte(); // instruments
                if (version >= 3) {
                    length = readShort(dataInputStream);
                }
            }
            layerCount = readShort(dataInputStream);
            title = readString(dataInputStream);
            author = readString(dataInputStream);
            originalAuthor = readString(dataInputStream); // original author
            description = readString(dataInputStream);
            tempo = readShort(dataInputStream) / 100f;
            dataInputStream.readBoolean(); // auto-save
            dataInputStream.readByte(); // auto-save duration
            timeSignature = dataInputStream.readByte(); // x/4ths, time signature
            readInt(dataInputStream); // minutes spent on project
            readInt(dataInputStream); // left clicks (why?)
            readInt(dataInputStream); // right clicks (why?)
            readInt(dataInputStream); // blocks added
            readInt(dataInputStream); // blocks removed
            readString(dataInputStream); // .mid/.schematic file name
            if (version >= 4) {
                enableLoop = dataInputStream.readByte() != 0; // loop on/off
                maxLoopCount = dataInputStream.readByte(); // max loop count
                loopStartTick = readShort(dataInputStream); // loop start tick
            }
            short tick = -1;
            while (true) {
                short jumpTicks = readShort(dataInputStream); // jumps till next tick
                //System.out.println("Jumps to next tick: " + jumpTicks);
                if (jumpTicks == 0) {
                    break;
                }
                tick += jumpTicks;
                //System.out.println("Tick: " + tick);
                short layer = -1;
                while (true) {
                    short jumpLayers = readShort(dataInputStream); // jumps till next layer
                    if (jumpLayers == 0) {
                        break;
                    }
                    layer += jumpLayers;
                    //System.out.println("Layer: " + layer);
                    byte instrument = dataInputStream.readByte();

                    if (instrument >= customInstrument) {
                        instrument = 0;
                    }

                    byte key = dataInputStream.readByte();
                    byte volume = 100;
                    int panning = 100;
                    short pitch = 0;
                    if (version >= 4) {
                        volume = dataInputStream.readByte(); // note block velocity
                        panning = 200 - dataInputStream.readUnsignedByte(); // note panning, 0 is right in nbs format
                        pitch = readShort(dataInputStream); // note block pitch
                    }

                    if (panning != 100){
                        isStereo = true;
                    }

                    setNote(layer, tick, new Note(instrument, key, volume, panning, pitch), layers);
                }
            }

            if (version > 0 && version < 3) {
                length = tick;
            }

            for (int i = 0; i < layerCount; i++) {
                Layer layer = layers.get(i);

                String name = readString(dataInputStream);
                if (version >= 4){
                    dataInputStream.readByte(); // layer lock
                }

                byte volume = dataInputStream.readByte();
                int panning = 100;
                if (version >= 2){
                    panning = 200 - dataInputStream.readUnsignedByte(); // layer stereo, 0 is right in nbs format
                }

                if (panning != 100){
                    isStereo = true;
                }

                if (layer != null) {
                    layer.setName(name);
                    layer.setVolume(volume);
                    layer.setPanning(panning);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.version = version;
        this.length = length;
        this.layerCount = layerCount;
        this.title = title;
        this.author = author;
        this.originalAuthor = originalAuthor;
        this.description = description;
        this.tempo = tempo;
        this.timeSignature = timeSignature;
        this.enableLoop = enableLoop;
        this.maxLoopCount = maxLoopCount;
        this.loopStartTick = loopStartTick;
        this.isStereo = isStereo;
    }

    private void setNote(int layerIndex, int ticks, Note note, HashMap<Integer, Layer> layerHashMap) {
        Layer layer = layerHashMap.get(layerIndex);
        if (layer == null) {
            layer = new Layer();
            layerHashMap.put(layerIndex, layer);
        }
        layer.setNote(ticks, note);
    }

    private short readShort(DataInputStream dataInputStream) throws IOException {
        int byte1 = dataInputStream.readUnsignedByte();
        int byte2 = dataInputStream.readUnsignedByte();
        return (short) (byte1 + (byte2 << 8));
    }

    private int readInt(DataInputStream dataInputStream) throws IOException {
        int byte1 = dataInputStream.readUnsignedByte();
        int byte2 = dataInputStream.readUnsignedByte();
        int byte3 = dataInputStream.readUnsignedByte();
        int byte4 = dataInputStream.readUnsignedByte();
        return (byte1 + (byte2 << 8) + (byte3 << 16) + (byte4 << 24));
    }

    private String readString(DataInputStream dataInputStream) throws IOException {
        int length = readInt(dataInputStream);
        StringBuilder builder = new StringBuilder(length);
        for (; length > 0; --length) {
            char c = (char) dataInputStream.readByte();
            if (c == (char) 0x0D) {
                c = ' ';
            }
            builder.append(c);
        }
        return builder.toString();
    }
}

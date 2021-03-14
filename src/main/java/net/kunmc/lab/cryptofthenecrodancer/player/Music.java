package net.kunmc.lab.cryptofthenecrodancer.player;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Music {
    private byte version;
    private byte instrument;
    private short length;
    private short layerCount;
    private String title;
    private String author;
    private String originalAuthor;
    private String description;
    private short tempo;
    private boolean autoSave;
    private byte autoSaveDuration;
    private byte timeSignature;
    private int minutesSpent;
    private int leftClicks;
    private int rightClicks;
    private int noteBlockAdded;
    private int noteBlockRemoved;
    private String importedFileName;
    private boolean enableLoop;
    private byte maxLoopCount;
    private short loopStartTick;
    private HashMap<Integer, Layer> layers;
    private boolean isStereo;

    public Music(InputStream inputStream) throws IOException {
        DataInputStream stream = new DataInputStream(inputStream);

        if (stream.readShort() == 0) {
            version = stream.readByte();
            instrument = stream.readByte();
            if (version >= 3) {
                length = stream.readShort();
            } else {
                length = 0;
            }
        } else {
            version = 0;
            instrument = 10;
            length = 0;
        }

        layerCount = stream.readShort();
        title = readString(stream);
        author = readString(stream);
        originalAuthor = readString(stream);
        description = readString(stream);
        tempo = stream.readShort();
        autoSave = stream.readBoolean();
        autoSaveDuration = stream.readByte();
        timeSignature = stream.readByte();
        minutesSpent = stream.readInt();
        leftClicks = stream.readInt();
        rightClicks = stream.readInt();
        noteBlockAdded = stream.readInt();
        noteBlockRemoved = stream.readInt();
        importedFileName = readString(stream);

        if (version >= 4) {
            enableLoop = stream.readBoolean();
            maxLoopCount = stream.readByte();
            loopStartTick = stream.readShort();
        } else {
            enableLoop = false;
            maxLoopCount = 0;
            loopStartTick = 0;
        }

        short tick = -1;
        for (short jumpTicks; (jumpTicks = stream.readShort()) != 0; ) {
            tick += jumpTicks;

            short layer = -1;
            for (short jumpLayers; (jumpLayers = stream.readShort()) != 0; ) {
                layer += jumpLayers;

                byte instrument = stream.readByte();
                byte key = stream.readByte();
                byte volume = 100;
                int panning = 100;
                short pitch = 0;
                if (version >= 4) {
                    volume = stream.readByte();
                    panning = 200 - stream.readUnsignedByte();
                    pitch = stream.readShort();
                }

                if (panning != 100) {
                    isStereo = true;
                }

                setNote(layer, tick, new Note(instrument, key, volume, panning, pitch));
            }
        }

        if (0 < version && version < 3) {
            length = tick;
        }

        for (int i = 0; i < layerCount; i++) {
            Layer layer = layers.get(i);
            String name = readString(stream);

            if (version >= 4) {
                // Layer locked
                stream.readBoolean();
            }

            byte volume = stream.readByte();
            int panning = 100;
            if (version >= 2) {
                panning = stream.readByte();
            }

            if (panning != 100) {
                isStereo = true;
            }

            if (layer != null) {
                layer.setName(name);
                layer.setVolume(volume);
                layer.setPanning(panning);
            }
        }

        // custom instrument
    }

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

    public short getTempo() {
        return tempo;
    }

    public byte getTimeSignature() {
        return timeSignature;
    }

    public boolean isStereo() {
        return isStereo;
    }

    private void setNote(int layerIndex, int tick, Note note) {
        Layer layer = layers.get(layerIndex);
        if (layer == null) {
            layer = new Layer();
            layers.put(layerIndex, layer);
        }

        layer.setNote(tick, note);
    }

    private String readString(DataInputStream stream) throws IOException {
        int length = stream.readInt();

        StringBuilder builder;
        for(builder = new StringBuilder(length); length > 0; --length) {
            char c = (char)stream.readByte();
            if (c == '\r') {
                c = ' ';
            }

            builder.append(c);
        }

        return builder.toString();
    }
}

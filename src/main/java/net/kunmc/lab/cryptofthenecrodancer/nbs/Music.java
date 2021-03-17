package net.kunmc.lab.cryptofthenecrodancer.nbs;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Music {
    private final byte version;
    private final byte instrument;
    private final short length;
    private final short layerCount;
    private final String title;
    private final String author;
    private final String originalAuthor;
    private final String description;
    private final float tempo;
    private final boolean autoSave;
    private final byte autoSaveDuration;
    private final byte timeSignature;
    private final int minutesSpent;
    private final int leftClicks;
    private final int rightClicks;
    private final int noteBlockAdded;
    private final int noteBlockRemoved;
    private final String importedFileName;
    private final boolean enableLoop;
    private final byte maxLoopCount;
    private final short loopStartTick;
    private final HashMap<Integer, Layer>  layers = new HashMap<>();
    private final boolean isStereo;

    public Music(InputStream inputStream) throws IOException {
        DataInputStream stream = new DataInputStream(inputStream);
        short length = 0;
        boolean isStereo = false;

        if (stream.readShort() == 0) {
            version = stream.readByte();
            instrument = stream.readByte();
            if (version >= 3) {
                length = stream.readShort();
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
        tempo = stream.readShort() / 100.0f;
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

        this.length = length;
        this.isStereo = isStereo;
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

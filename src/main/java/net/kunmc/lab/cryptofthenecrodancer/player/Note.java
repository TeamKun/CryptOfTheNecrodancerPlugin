package net.kunmc.lab.cryptofthenecrodancer.player;

public class Note {
    private byte instrument;
    private byte key;
    private byte volume;
    private int panning;
    private short pitch;

    public Note(byte instrument, byte key) {
        this(instrument, key, (byte)100, 100, (short)0);
    }

    public Note(byte instrument, byte key, byte volume, int panning, short pitch) {
        this.instrument = instrument;
        this.key = key;
        this.volume = volume;
        this.panning = panning;
        this.pitch = pitch;
    }

    public byte getInstrument() {
        return this.instrument;
    }

    public void setInstrument(byte instrument) {
        this.instrument = instrument;
    }

    public byte getKey() {
        return this.key;
    }

    public void setKey(byte key) {
        this.key = key;
    }

    public short getPitch() {
        return this.pitch;
    }

    public void setPitch(short pitch) {
        this.pitch = pitch;
    }

    public byte getVolume() {
        return this.volume;
    }

    public void setVolume(byte volume) {
        if (volume < 0) {
            volume = 0;
        }

        if (volume > 100) {
            volume = 100;
        }

        this.volume = volume;
    }

    public int getPanning() {
        return this.panning;
    }

    public void setPanning(int panning) {
        this.panning = panning;
    }
}

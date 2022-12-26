package jaygoo.library.converter;

/**
 * ================================================
 * 作    者：JayGoo
 * 版    本：
 * 创建日期：2018/4/18
 * 描    述:use lame to convert file to mp3
 * ================================================
 */
public class Mp3Converter {

    static  {
        System.loadLibrary("lame-mp3-utils");
    }

    /**
     * init lame
     * @param inSampleRate
     *              input sample rate in Hz
     * @param channel
     *              number of channels
     * @param mode
     *              0 = CBR, 1 = VBR, 2 = ABR.  default = 0
     * @param outSampleRate
     *              output sample rate in Hz
     * @param outBitRate
     *              rate compression ratio in KHz
     * @param quality
     *              quality=0..9. 0=best (very slow). 9=worst.<br />
     *              recommended:<br />
     *              2 near-best quality, not too slow<br />
     *              5 good quality, fast<br />
     *              7 ok quality, really fast
     */
    public native static void init(int inSampleRate, int channel, int mode,
                                   int outSampleRate, int outBitRate, int quality);


    /**
     * file convert to mp3
     * it may cost a lot of time and better put it in a thread
     * @param inputPath
     *          file path to be converted
     * @param mp3Path
     *          mp3 output file path
     */
    public native  static void convertMp3(String inputPath, String mp3Path);

    /**
     * Encode buffer to mp3.
     *
     * @param bufferLeft
     *            PCM data for left channel.
     * @param bufferRight
     *            PCM data for right channel.
     * @param samples
     *            number of samples per channel.
     * @param mp3buf
     *            result encoded MP3 stream. You must specified
     *            "7200 + (1.25 * buffer_l.length)" length array.
     * @return <p>number of bytes output in mp3buf. Can be 0.</p>
     *         <p>-1: mp3buf was too small</p>
     *         <p>-2: malloc() problem</p>
     *         <p>-3: lame_init_params() not called</p>
     *         -4: psycho acoustic problems
     */
    public native static int encode(short[] bufferLeft, short[] bufferRight,
        int samples, byte[] mp3buf);

    /**
     * Flush LAME buffer.
     *
     * REQUIRED:
     * lame_encode_flush will flush the intenal PCM buffers, padding with
     * 0's to make sure the final frame is complete, and then flush
     * the internal MP3 buffers, and thus may return a
     * final few mp3 frames.  'mp3buf' should be at least 7200 bytes long
     * to hold all possible emitted data.
     *
     * will also write id3v1 tags (if any) into the bitstream
     *
     * return code = number of bytes output to mp3buf. Can be 0
     * @param mp3buf
     *            result encoded MP3 stream. You must specified at least 7200
     *            bytes.
     * @return number of bytes output to mp3buf. Can be 0.
     */
    public native static int flush(byte[] mp3buf);

    /**
     * Close LAME.
     */
    public native static void close();

    /**
     * get converted bytes in inputBuffer
     * @return
     *          converted bytes in inputBuffer
     *          to ignore the deviation of the file size,when return to -1 represents convert complete
     */
    public native static long getConvertBytes();

    /**
     * get library lame version
     * @return
     */
    public native static String getLameVersion();

}

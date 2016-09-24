package com.kbrobot.utils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;
import it.sauronsoftware.jave.VideoAttributes;
import it.sauronsoftware.jave.VideoSize;
public class AudioFileFormatUtil {
	
	/**
	 * 音频格式转换
	 * @param src
	 * @param target
	 * @throws IllegalArgumentException
	 * @throws InputFormatException
	 * @throws EncoderException
	 */
	public static void wav2mp3(String src, String target) throws IllegalArgumentException, InputFormatException, EncoderException{
		File souceFile = new File(src);
		File targetFile = new File(target);
		
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("libmp3lame");
		audio.setBitRate(new Integer(64000));
		audio.setChannels(new Integer(1));
		audio.setSamplingRate(new Integer(22050));
		VideoAttributes video = new VideoAttributes();
		video.setCodec("wav");
		video.setBitRate(new Integer(160000));
		video.setFrameRate(new Integer(15));
		video.setSize(new VideoSize(400, 300));
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("mp3");
		attrs.setAudioAttributes(audio);
		attrs.setVideoAttributes(video);
		Encoder encoder = new Encoder();
		encoder.encode(souceFile, targetFile, attrs);
	}
	
	public static void wav2amr(String src,String target) throws IllegalArgumentException, InputFormatException, EncoderException{
		File souceFile = new File(src);
		File targetFile = new File(target);
		
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("libamr_nb");
		audio.setBitRate(new Integer(12200));
		audio.setChannels(new Integer(1));
		audio.setSamplingRate(new Integer(8000));
		
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("amr");
		attrs.setAudioAttributes(audio);
		Encoder encoder = new Encoder();
		
		encoder.encode(souceFile, targetFile, attrs);
	}
	
	
	/**
	 * 讯飞录音文件格式转换
	 * @param src
	 * @param target
	 * @throws Exception
	 */
	public static void pcm2wav(String src, String target) throws Exception {
		FileInputStream fis = new FileInputStream(src);
		FileOutputStream fos = new FileOutputStream(target);
		//计算长度
		byte[] buf = new byte[1024 * 4];
		int size = fis.read(buf);
		int PCMSize = 0;
		while (size != -1) {
			PCMSize += size;
			size = fis.read(buf);
		}
		fis.close();
		//填入参数，比特率等等。这里用的是16位单声道 8000 hz
		WaveHeader header = new WaveHeader();
		//长度字段 = 内容的大小（PCMSize) + 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
		header.fileLength = PCMSize + (44 - 8);
		header.FmtHdrLeth = 16;
		header.BitsPerSample = 16;
		header.Channels = 1;
		header.FormatTag = 0x0001;
		header.SamplesPerSec = 16000;
		header.BlockAlign = (short)(header.Channels * header.BitsPerSample / 8);
		header.AvgBytesPerSec = header.BlockAlign * header.SamplesPerSec;
		header.DataHdrLeth = PCMSize;
		byte[] h = header.getHeader();
		assert h.length == 44; //WAV标准，头部应该是44字节
		//write header
		fos.write(h, 0, h.length);
		//write data stream
		fis = new FileInputStream(src);
		size = fis.read(buf);
		while (size != -1) {
			fos.write(buf, 0, size);
			size = fis.read(buf);
		}
		fis.close();
		fos.close();
		System.out.println("Convert OK!");
	}

	//WavHeader辅助类。用于生成头部信息。
	public static class WaveHeader {
		public final char fileID[] = {'R', 'I', 'F', 'F'};
		public int fileLength;
		public char wavTag[] = {'W', 'A', 'V', 'E'};;
		public char FmtHdrID[] = {'f', 'm', 't', ' '};
		public int FmtHdrLeth;
		public short FormatTag;
		public short Channels;
		public int SamplesPerSec;
		public int AvgBytesPerSec;
		public short BlockAlign;
		public short BitsPerSample;
		public char DataHdrID[] = {'d','a','t','a'};
		public int DataHdrLeth;
		public byte[] getHeader() throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			WriteChar(bos, fileID);
			WriteInt(bos, fileLength);
			WriteChar(bos, wavTag);
			WriteChar(bos, FmtHdrID);
			WriteInt(bos,FmtHdrLeth);
			WriteShort(bos,FormatTag);
			WriteShort(bos,Channels);
			WriteInt(bos,SamplesPerSec);
			WriteInt(bos,AvgBytesPerSec);
			WriteShort(bos,BlockAlign);
			WriteShort(bos,BitsPerSample);
			WriteChar(bos,DataHdrID);
			WriteInt(bos,DataHdrLeth);
			bos.flush();
			byte[] r = bos.toByteArray();
			bos.close();
			return r;
		}
		private void WriteShort(ByteArrayOutputStream bos, int s) throws IOException {
			byte[] mybyte = new byte[2];
			mybyte[1] =(byte)( (s << 16) >> 24 );
			mybyte[0] =(byte)( (s << 24) >> 24 );
			bos.write(mybyte);
		}
		private void WriteInt(ByteArrayOutputStream bos, int n) throws IOException {
			byte[] buf = new byte[4];
			buf[3] =(byte)( n >> 24 );
			buf[2] =(byte)( (n << 8) >> 24 );
			buf[1] =(byte)( (n << 16) >> 24 );
			buf[0] =(byte)( (n << 24) >> 24 );
			bos.write(buf);
		}
		private void WriteChar(ByteArrayOutputStream bos, char[] id) {
			for (int i=0; i<id.length; i++) {
				char c = id[i];
				bos.write(c);
			}
		}
	}
}

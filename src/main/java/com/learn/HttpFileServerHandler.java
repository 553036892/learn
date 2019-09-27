package com.learn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private final String url;

	public HttpFileServerHandler(String url) {
		this.url = url;
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		if (!request.decoderResult().isSuccess()) {
			sendError(ctx, BAD_REQUEST);
			return;
		}
		if (request.method() != HttpMethod.GET) {
			sendError(ctx, METHOD_NOT_ALLOWED);
			return;
		}
		final String uri = request.uri();
		final String path = sanitizeUri(uri);
		if (path == null) {
			sendError(ctx, FORBIDDEN);
			return;
		}
		File file = new File(path);
		if (file.isHidden() || !file.exists()) {
			sendError(ctx, NOT_FOUND);
			return;
		}
		if (file.isDirectory()) {
			if (uri.endsWith("/")) {
				sendListing(ctx, file);
			} else {
				sendRedirect(ctx, uri + '/');
			}
			return;
		}
		if (!file.isFile()) {
			sendError(ctx, FORBIDDEN);
			return;
		}
		RandomAccessFile randomAccessFile = null;
		try {
			randomAccessFile = new RandomAccessFile(file, "r");
		} catch (FileNotFoundException e) {
			sendError(ctx, NOT_FOUND);
			return;
		}
		long fileLenth = randomAccessFile.length();
		HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
		setContentLength(response, fileLenth);
		setContentTypeHeader(response, file);
		if (isKeepAlive(request)) {
			response.headers().set(CONNECTION, "keep_alive");
		}
		ctx.write(response);
		ChannelFuture sendFileFutrue;
		sendFileFutrue = ctx.write(new ChunkedFile(randomAccessFile, 0, fileLenth, 8192), ctx.newProgressivePromise());
		sendFileFutrue.addListener(new ChannelProgressiveFutureListener() {

			@Override
			public void operationComplete(ChannelProgressiveFuture future) throws Exception {
				System.out.println("Transfer complete.");
			}

			@Override
			public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
				if (total < 0) {
					System.err.println("Transfer progress:" + progress);
				} else {
					System.err.println("Transfer progress:" + progress + "/" + total);
				}
			}
		});
		ChannelFuture lastContentFutre = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
		if (!isKeepAlive(request)) {
			lastContentFutre.addListener(ChannelFutureListener.CLOSE);
		}
	}

	private void setContentLength(HttpResponse response, long fileLenth) {
		response.headers().set("content-length", Long.toString(fileLenth));
	}

	private boolean isKeepAlive(FullHttpRequest request) {
		return true;
	}

	private void setContentTypeHeader(HttpResponse response, File file) {
		MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
		response.headers().set(CONTENT_TYPE, mimeTypesMap.getContentType(file.getPath()));
	}

	private static final Pattern AOOLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

	private static final HttpResponseStatus FORBIDDEN = HttpResponseStatus.FORBIDDEN;

	private static final HttpResponseStatus NOT_FOUND = HttpResponseStatus.NOT_FOUND;

	private static final HttpResponseStatus METHOD_NOT_ALLOWED = HttpResponseStatus.METHOD_NOT_ALLOWED;

	private static final HttpResponseStatus BAD_REQUEST = HttpResponseStatus.BAD_REQUEST;

	private static final HttpResponseStatus OK = HttpResponseStatus.OK;

	private static final String CONTENT_TYPE = "content-type";

	private static final String LOCATION = "location";

	private static final String CONNECTION = "connection";

	private void sendListing(ChannelHandlerContext ctx, File dir) throws UnsupportedEncodingException {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
		response.headers().set(CONTENT_TYPE, "text/html;charset=UTF-8");
		StringBuilder buf = new StringBuilder();
		String dirPath = dir.getPath();
		buf.append("<!DOCTYPE html>\r\n");
		buf.append("<html><head><title>");
		buf.append(dirPath);
		buf.append(" 目录：");
		buf.append("</title></head><body>\r\n");
		buf.append("<h3>");
		buf.append(dirPath).append(" 目录：");
		buf.append("</h3>\r\n");
		buf.append("<ul>");
		buf.append("<li>上一级：<a href=\"../\">..</a></li>\r\n");
		for (File f : dir.listFiles()) {
			if (f.isHidden() || !f.canRead()) {
				continue;
			}
			String name = f.getName();
			if (!AOOLOWED_FILE_NAME.matcher(name).matches()) {
				continue;
			}
			if(f.isDirectory()) {
				buf.append("<li>目录：<a href=\"");
			}else {
				buf.append("<li>文件：<a href=\"");
			}
			buf.append(name);
			buf.append("\">");
			buf.append(name);
			buf.append("</a></li>\r\n");
		}
		buf.append("<ul></body></html>\r\n");
		ByteBuf buffer = Unpooled.copiedBuffer(buf.toString().getBytes("UTF-8"));
		response.content().writeBytes(buffer);
		buffer.release();
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	private static final HttpResponseStatus FOUND = HttpResponseStatus.FOUND;

	private static void sendRedirect(ChannelHandlerContext ctx, String newUri) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
		response.headers().set(LOCATION, newUri);
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	private static final HttpVersion HTTP_1_1 = HttpVersion.HTTP_1_1;

	private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status,
				Unpooled.copiedBuffer("Failure:  " + status.toString() + "\r\n", CharsetUtil.UTF_8));
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

	private String sanitizeUri(String uri) {
		try {
			uri = URLDecoder.decode(uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			try {
				uri = URLDecoder.decode(uri, "ISO-8859-1");
			} catch (UnsupportedEncodingException e1) {
				throw new Error();
			}
		}
//		if (!uri.startsWith(url)) {
//			return null;
//		}
		if (!uri.startsWith("/")) {
			return null;
		}
		uri = uri.replace('/', File.separatorChar);
		if (uri.contains(File.separator + '.') || uri.contains('.' + File.separator) || uri.startsWith(".")
				|| uri.endsWith(".") || INSECURE_URI.matcher(uri).matches()) {
			return null;
		}
		return System.getProperty("user.dir") + File.separator + uri;
	}

}

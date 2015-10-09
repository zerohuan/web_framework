package com.yjh.base.filter;

import com.yjh.base.util.StringUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

/**
 * 1.check request Headers:
 * Accept-Encoding: gzip
 * 2.set response Header if client supports gzip:
 * Content-Encoding: gzip
 * 3.use stream of gzip by inner class:
 * java.util.zip.GZIPOutputStream
 * 4.modify response content：
 * wrapper object of response before doFilter()
 *
 * Created by yjh on 2015/9/15.
 */
public class CompressFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        //check Accept-Encoding Header contains gzip or not
        String acceptEncodingHeader = request.getHeader("Accept-Encoding");
        if(!StringUtil.isEmpty(acceptEncodingHeader) && acceptEncodingHeader.toLowerCase().contains("gzip")) {
            //add response header
            response.setHeader("Content-Encoding", "gzip");
            //compress
            //you can see decoration entities is a popular design entities
            ResponseWrapper wrapper = new ResponseWrapper(response);
            //doFilter, close writer
            try {
                filterChain.doFilter(request, wrapper);
            } finally {
                try {
                    wrapper.finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else {
            //without compress
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    public void destroy() {

    }

    /**
     * before find using anywhere else, I use inner class to wrap response
     *
     * @author yjh
     */
    private static class ResponseWrapper extends HttpServletResponseWrapper {
        private GZIPServletOutputStream outputStream;
        private PrintWriter writer;

        public ResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        /**
         * There are getOutputStream() and getWriter(), obviously, only one of can be called in a request.
         *
         * @return GZIPServletOutputStream wrap original outputStream
         * @throws IOException
         */
        @Override
        public synchronized ServletOutputStream getOutputStream() throws IOException {
            if(this.writer != null)
                throw new IllegalStateException("getWriter() already called.");
            if(this.outputStream == null) //lazy initialization
                this.outputStream = new GZIPServletOutputStream(super.getOutputStream());
            return this.outputStream;
        }

        /**
         * @see ResponseWrapper#getOutputStream()
         * @return create a new printWriter with GZIPServletOutputStream
         * @throws IOException
         */
        @Override
        public synchronized PrintWriter getWriter() throws IOException {
            if(this.writer == null && this.outputStream != null) {
                throw new IllegalStateException("getOutputStream() ");
            }
            if(this.writer == null) {
                this.outputStream = new GZIPServletOutputStream(super.getOutputStream());
                this.writer = new PrintWriter(new OutputStreamWriter(this.outputStream, this.getCharacterEncoding()));
            }
            return this.writer;
        }

        @Override
        public void flushBuffer() throws IOException {
            if(this.writer != null) {
                this.writer .flush();
            } else if(this.outputStream != null) {
                this.outputStream.flush();
            }
            //always call super.flushBuffer
            super.flushBuffer();
        }

        /**
         * Avoid set content-length before compressing,override setContentLength(), setContentLengthLong, setHeader,
         * addHeader, setIntHeader, addIntHeader
         *
         * @param len
         */
        @Override
        public void setContentLength(int len) {
        }

        /**
         * @see ResponseWrapper#setContentLength(int)
         * @param len
         */
        @Override
        public void setContentLengthLong(long len) {
        }

        /**
         * @see ResponseWrapper#setContentLength(int)
         * @param name
         * @param value
         */
        @Override
        public void setHeader(String name, String value) {
            if(!"content-length".equalsIgnoreCase(name))
                super.setHeader(name, value);
        }

        /**
         * @see ResponseWrapper#setContentLength(int)
         * @param name
         * @param value
         */
        @Override
        public void addHeader(String name, String value) {
            if(!"content-length".equalsIgnoreCase(name))
                super.addHeader(name, value);
        }

        /**
         * @see ResponseWrapper#setContentLength(int)
         * @param name
         * @param value
         */
        @Override
        public void setIntHeader(String name, int value) {
            if(!"content-length".equalsIgnoreCase(name))
                super.setIntHeader(name, value);
        }

        /**
         * @see ResponseWrapper#setContentLength(int)
         * @param name
         * @param value
         */
        @Override
        public void addIntHeader(String name, int value) {
            if(!"content-length".equalsIgnoreCase(name))
                super.addIntHeader(name, value);
        }

        /**
         * Call finish when using getOutputStream, this Method just using in applying multiple filters
         * in succession to the same output stream.
         *
         *  @see GZIPServletOutputStream#finish()
         * @throws IOException
         */
        public void finish() throws IOException {
            if(this.writer != null)
                this.writer.close();
            else if(this.outputStream != null) {
                this.outputStream.finish();
            }
        }
    }

    /**
     * wrap servletOutputStream
     *
     * @author yjh
     */
    private static class GZIPServletOutputStream extends ServletOutputStream {
        private final ServletOutputStream servletOutputStream;
        private final GZIPOutputStream gzipOutputStream;

        public GZIPServletOutputStream(ServletOutputStream servletOutputStream)
                throws IOException {
            this.servletOutputStream = servletOutputStream;
            this.gzipOutputStream = new GZIPOutputStream(servletOutputStream);
        }

        @Override
        public void write(int b) throws IOException {
            this.gzipOutputStream.write(b);
        }

        @Override
        public boolean isReady() {
            return this.servletOutputStream.isReady();
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            this.servletOutputStream.setWriteListener(writeListener);
        }

        @Override
        public void close() throws IOException {
            this.gzipOutputStream.close();
        }

        @Override
        public void flush() throws IOException {
            this.gzipOutputStream.flush();
        }

        public void finish() throws IOException {
            this.gzipOutputStream.finish();
        }
    }


}

package com.example.satyam.myapplication;

/**
 * Created by satyam on 9/24/16.
 */
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class CustomMultiPartEntity extends MultipartEntity
{

    private UploadProgressListener uploadProgressListener;

    public CustomMultiPartEntity()
    {
        super();

    }

    public CustomMultiPartEntity(final HttpMultipartMode mode)
    {
        super(mode);

    }

    public CustomMultiPartEntity(HttpMultipartMode mode, final String boundary, final Charset charset)
    {
        super(mode, boundary, charset);

    }

    @Override
    public void writeTo(final OutputStream outstream) throws IOException
    {
        super.writeTo(new CountingOutputStream(outstream, this.uploadProgressListener));


    }

    /**
     *
     * @return
     */
    public UploadProgressListener getUploadProgressListener() {
        return uploadProgressListener;
    }
    /**
     *
     * @param uploadProgressListener
     */
    public void setUploadProgressListener(
            UploadProgressListener uploadProgressListener) {
        this.uploadProgressListener = uploadProgressListener;
    }


    /**
     *
     * Count the OutputStream
     *
     */
    public static class CountingOutputStream extends FilterOutputStream
    {

        private final UploadProgressListener uploadProgressListener;
        private long transferred;

        public CountingOutputStream(final OutputStream out, final UploadProgressListener uploadProgressListener)
        {
            super(out);
            this.uploadProgressListener = uploadProgressListener;
            this.transferred = 0;
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException
        {
            out.write(b, off, len);
            this.transferred += len;

            if(this.uploadProgressListener !=null){

                this.uploadProgressListener.transferred(this.transferred);

            }

        }
        @Override
        public void write(int b) throws IOException
        {
            out.write(b);
            this.transferred++;

            if(this.uploadProgressListener !=null){

                this.uploadProgressListener.transferred(this.transferred);

            }

        }
    }
}
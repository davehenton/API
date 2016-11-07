package gov.ca.cwds.rest.jdbi.cms;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.DatatypeConverter;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.ca.cwds.rest.api.persistence.cms.CmsDocument;
import gov.ca.cwds.rest.api.persistence.cms.CmsDocumentBlobSegment;
import gov.ca.cwds.rest.jdbi.CmsCrudsDaoImpl;
import gov.ca.cwds.rest.util.jni.LZWEncoder;

public class CmsDocumentDao extends CmsCrudsDaoImpl<CmsDocument> {

  private static final Logger LOGGER = LoggerFactory.getLogger(CmsDocumentDao.class);

  /**
   * Constructor
   * 
   * @param sessionFactory The sessionFactory
   */
  public CmsDocumentDao(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  /**
   * Decompress a document by determining the compression type, assembling blob segments, and
   * calling native library.
   * 
   * @param doc
   * @return base64-encoded String of decompressed document
   */
  public static String decompressDoc(gov.ca.cwds.rest.api.persistence.cms.CmsDocument doc) {
    String retval = "";

    if (doc.getCompressionMethod().endsWith("01")) {
      if (!LZWEncoder.isClassloaded()) {
        LOGGER.warn("LZW compression not enabled!");
      } else {
        retval = CmsDocumentDao.decompressLZW(doc);
      }
    } else if (doc.getCompressionMethod().endsWith("02")) {
      LOGGER.warn("PK compression not enabled!");
    } else {
      LOGGER.warn("UNSUPPORTED compression method " + doc.getCompressionMethod());
    }

    return retval;
  }

  /**
   * Decompress an LZW-compressed document by assembling blob segments and calling native library.
   * 
   * @param doc
   * @return base64-encoded String of decompressed document
   */
  protected static String decompressLZW(gov.ca.cwds.rest.api.persistence.cms.CmsDocument doc) {
    String retval = "";
    LZWEncoder lzw = new LZWEncoder();
    try {
      File src = File.createTempFile("src", ".lzw");
      src.deleteOnExit();

      File tgt = File.createTempFile("tgt", ".doc");
      tgt.deleteOnExit();

      FileOutputStream fos = new FileOutputStream(src);
      for (CmsDocumentBlobSegment seg : doc.getBlobSegments()) {
        final byte[] bytes = DatatypeConverter.parseHexBinary(seg.getDocBlob().trim());
        fos.write(bytes, 0, bytes.length);
      }
      fos.flush();
      fos.close();

      // DECOMPRESS!
      // TODO: Trap std::exception in shared library and return error code.
      // Unhandled C++ exceptions kill the JVM.
      lzw.fileCopyUncompress(src.getAbsolutePath(), tgt.getAbsolutePath());

      Path path = Paths.get(tgt.getAbsolutePath());
      retval = DatatypeConverter.printBase64Binary(Files.readAllBytes(path));

      // For security reasons, remove temp documents immediately.
      src.delete();
      tgt.delete();

    } catch (Exception e) {
      LOGGER.error("ERROR DECOMPRESSING LZW! " + e.getMessage());
      throw new RuntimeException(e);
    }

    return retval;
  }

}

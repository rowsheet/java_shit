package com.VendorMedia;

import com.VendorMedia.PageImages.PageImageController;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by alexanderkleinhans on 3/31/18.
 */
@WebService
public class VendorMediaHandler {
    @WebMethod
    public int createVendorPageImageGallery(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "name") String name,
            @WebParam(name = "hex_color") String hex_color
    ) throws Exception {
        PageImageController pageImageController = new PageImageController();
        return pageImageController.createVendorPageImageGaller(
                cookie,
                name,
                hex_color
        );
    }

    @WebMethod
    public int uploadVendorPageImage (
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "file_path") String file_path,
            @WebParam(name = "gallery_id") int gallery_id
    ) throws Exception {
        PageImageController pageImageController = new PageImageController();
        return pageImageController.uploadVendorPageImage(
                cookie,
                file_path,
                gallery_id
        );
    }

    @WebMethod
    public boolean deleteVendorPageImage(
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "image_id") int image_id
    ) throws Exception {
        PageImageController pageImageController = new PageImageController();
        return pageImageController.deleteVendorPageImage(
                cookie,
                image_id
        );
    }

    @WebMethod
    public String loadVendorPageImageGalleryTable (
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "gallery_id") int gallery_id
    ) throws Exception {
        PageImageController pageImageController = new PageImageController();
        return pageImageController.loadVendorPageImageGalleryTable(
                cookie,
                gallery_id
        );
    }

    @WebMethod
    public boolean updateVendorPageImageGallery (
            @WebParam(name="cookie") String cookie,
            @WebParam(name="image_ids") int[] image_ids
    ) throws Exception {
        PageImageController pageImageController = new PageImageController();
        return pageImageController.updateVendorPageImageGallery(
                cookie,
                image_ids
        );
    }

    // For all galleries (plural).
    @WebMethod
    public String loadVendorPageImageGalleriesTable (
            @WebParam(name = "cookie") String cookie
    ) throws Exception {
        PageImageController pageImageController = new PageImageController();
        return pageImageController.loadVendorPageImageGalleriesTable(
                cookie
        );
    }

    // For all galleries (plural).
    @WebMethod
    public boolean updateVendorPageImageGalleries (
            @WebParam(name="cookie") String cookie,
            @WebParam(name="gallery_ids") int[] gallery_ids
    ) throws Exception {
        PageImageController pageImageController = new PageImageController();
        return pageImageController.updateVendorPageImageGalleries(
                cookie,
                gallery_ids
        );
    }

    @WebMethod
    public boolean deleteVendorPageImageGallery (
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "gallery_id") int gallery_id
    ) throws Exception {
        PageImageController pageImageController = new PageImageController();
        return pageImageController.deleteVendorPageImageGallery(
                cookie,
                gallery_id
        );
    }

    @WebMethod
    public boolean setMainGalleryID (
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "gallery_id") int gallery_id
    ) throws Exception {
        PageImageController pageImageController = new PageImageController();
        return pageImageController.setMainGalleryID(
                cookie,
                gallery_id
        );
    }

    @WebMethod
    public boolean setMainFoodGalleryID (
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "gallery_id") int gallery_id
    ) throws Exception {
        PageImageController pageImageController = new PageImageController();
        return pageImageController.setMainFoodGalleryID(
                cookie,
                gallery_id
        );
    }

    @WebMethod
    public boolean setMainDrinkGalleryID (
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "gallery_id") int gallery_id
    ) throws Exception {
        PageImageController pageImageController = new PageImageController();
        return pageImageController.setMainDrinkGalleryID(
                cookie,
                gallery_id
        );
    }

    @WebMethod
    public boolean setMainBeerGalleryID (
            @WebParam(name = "cookie") String cookie,
            @WebParam(name = "gallery_id") int gallery_id
    ) throws Exception {
        PageImageController pageImageController = new PageImageController();
        return pageImageController.setMainBeerGalleryID(
                cookie,
                gallery_id
        );
    }

}

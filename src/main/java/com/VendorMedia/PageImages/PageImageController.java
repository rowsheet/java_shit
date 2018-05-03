package com.VendorMedia.PageImages;

import com.Common.AbstractController;
import com.Common.VendorMedia.P_VendorPageImage;
import com.Common.VendorMedia.P_VendorPageImageGallery;
import com.Common.VendorMedia.P_VendorPageImageTable;
import com.Common.VendorMedia.VendorPageImageGallery;
import jnr.ffi.annotations.In;
import sun.jvm.hotspot.debugger.Page;

import java.util.HashMap;

public class PageImageController extends AbstractController{

    public PageImageController() throws Exception {}

    public int createVendorPageImageGaller(
        String cookie,
        String name,
        String hex_color
    ) throws Exception {
        // Validate cookie.
        this.validateString(cookie, "cookie");
        this.validateString(name, "name");
        this.validateHexColor(hex_color);
        // Create model and presist data.
        PageImageModel pageImageModel = new PageImageModel();
        return pageImageModel.createVendorPageImageGallery(
                cookie,
                name,
                hex_color
        );
    }

    public int uploadVendorPageImage(
            String cookie,
            String file_path,
            int gallery_id
    ) throws Exception {
        // Validate things.
        this.validateString(cookie, "cookie");
        this.validateString(file_path, "file_path");
        this.validateID(gallery_id, "gallery_id");
        // Model things.
        PageImageModel pageImageModel = new PageImageModel();
        return pageImageModel.uploadVendorPageImage(
                cookie,
                file_path,
                gallery_id
        );
    }

    public boolean deleteVendorPageImage (
            String cookie,
            int image_id
    ) throws Exception {
        // Validate cookie.
        this.validateString(cookie, "cookie");
        this.validateID(image_id, "image_id");
        // Model things.
        PageImageModel pageImageModel = new PageImageModel();
        return  pageImageModel.deleteVendorPageImage(
                cookie,
                image_id
        );
    }

    public String loadVendorPageImageGalleryTable (
            String cookie,
            int gallery_id
    ) throws Exception {
        // Validate cookie.
        this.validateString(cookie, "cookie");
        this.validateID(gallery_id, "gallery_id");
        // Model things.
        PageImageModel pageImageModel = new PageImageModel();
        P_VendorPageImageTable p_vendorPageImageTable = pageImageModel.loadVendorPageImageGalleryTable(
                cookie,
                gallery_id
        );
        return this.returnJSON(p_vendorPageImageTable);
    }

    public boolean updateVendorPageImageGallery (
            String cookie,
            int[] image_ids
    ) throws Exception {
        // Validate cookie.
        this.validateString(cookie, "cookie");
        for (int i = 0; i < image_ids.length; i++) {
            this.validateID(image_ids[i], "image_id");
        }
        // Model things.
        PageImageModel pageImageModel = new PageImageModel();
        return pageImageModel.updateVendorPageImageGallery(
                cookie,
                image_ids
        );
    }

    public String loadVendorPageImageGalleriesTable (
            String cookie
    ) throws Exception {
        // Validate cookie.
        this.validateString(cookie, "cookie");
        // Model things.
        PageImageModel pageImageModel = new PageImageModel();
        HashMap<Integer, P_VendorPageImageGallery> vendorPageImageGalleryHashMap = new HashMap<Integer, P_VendorPageImageGallery>();
        vendorPageImageGalleryHashMap = pageImageModel.loadVendorPageImageGalleriesTable(
                cookie
        );
        return this.returnJSON(vendorPageImageGalleryHashMap);
    }

    public boolean updateVendorPageImageGalleries (
            String cookie,
            int[] gallery_ids
    ) throws Exception {
        // Validate cookie.
        this.validateString(cookie, "cookie");
        for (int i = 0; i < gallery_ids.length; i++) {
            this.validateID(gallery_ids[i], "gallery_id");
        }
        // Model things.
        PageImageModel pageImageModel = new PageImageModel();
        return pageImageModel.updateVendorPageImageGalleries(
                cookie,
                gallery_ids
        );
    }

    public boolean deleteVendorPageImageGallery (
            String cookie,
            int gallery_id
    ) throws Exception {
        // Validate cookie.
        this.validateString(cookie, "cookie");
        this.validateID(gallery_id, "gallery_id");
        // Model things.
        PageImageModel pageImageModel = new PageImageModel();
        return  pageImageModel.deleteVendorPageImageGallery(
                cookie,
                gallery_id
        );
    }

    public boolean setMainImageID (
            String cookie,
            int image_id
    ) throws Exception {
        // Validate cookie.
        this.validateString(cookie, "cookie");
        this.validateID(image_id, "image_id");
        // Model things.
        PageImageModel pageImageModel = new PageImageModel();
        return  pageImageModel.setMainImageID(
                cookie,
                image_id
        );
    }

    public boolean setMainGalleryID (
            String cookie,
            int gallery_id
    ) throws Exception {
        // Validate cookie.
        this.validateString(cookie, "cookie");
        this.validateID(gallery_id, "gallery_id");
        // Model things.
        PageImageModel pageImageModel = new PageImageModel();
        return  pageImageModel.setMainGalleryID(
                cookie,
                gallery_id
        );
    }
    public boolean setMainFoodGalleryID (
            String cookie,
            int gallery_id
    ) throws Exception {
        // Validate cookie.
        this.validateString(cookie, "cookie");
        this.validateID(gallery_id, "gallery_id");
        // Model things.
        PageImageModel pageImageModel = new PageImageModel();
        return  pageImageModel.setMainFoodGalleryID(
                cookie,
                gallery_id
        );
    }
    public boolean setMainDrinkGalleryID (
            String cookie,
            int gallery_id
    ) throws Exception {
        // Validate cookie.
        this.validateString(cookie, "cookie");
        this.validateID(gallery_id, "gallery_id");
        // Model things.
        PageImageModel pageImageModel = new PageImageModel();
        return  pageImageModel.setMainDrinkGalleryID(
                cookie,
                gallery_id
        );
    }
    public boolean setMainBeerGalleryID (
            String cookie,
            int gallery_id
    ) throws Exception {
        // Validate cookie.
        this.validateString(cookie, "cookie");
        this.validateID(gallery_id, "gallery_id");
        // Model things.
        PageImageModel pageImageModel = new PageImageModel();
        return  pageImageModel.setMainBeerGalleryID(
                cookie,
                gallery_id
        );
    }
}

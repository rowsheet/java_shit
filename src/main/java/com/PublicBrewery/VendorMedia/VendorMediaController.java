package com.PublicBrewery.VendorMedia;

import com.Common.AbstractController;
import com.Common.VendorMedia.VendorMedia;
import com.Common.VendorMedia.VendorPageImageGallery;

import java.util.HashMap;

public class VendorMediaController extends AbstractController {

    public VendorMediaController() throws Exception {}

    public String loadVendorMedia(
            int vendor_id
    ) throws Exception {
        this.validateID(vendor_id, "vendor_id");
        VendorMediaModel vendorMediaModel = new VendorMediaModel();
        VendorMedia vendorMedia = vendorMediaModel.loadVendorMedia(vendor_id);
        return this.returnJSON(vendorMedia);
    }

    public String loadVendorPageImageGallery(
            int gallery_id
    ) throws Exception {
        this.validateID(gallery_id, "gallery_id");
        VendorMediaModel vendorMediaModel = new VendorMediaModel();
        VendorPageImageGallery vendorPageImageGallery = vendorMediaModel.loadVendorPageImageGallery(
                gallery_id
        );
        return this.returnJSON(vendorPageImageGallery);
    }

}

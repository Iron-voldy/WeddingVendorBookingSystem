package com.se1020.weddingvendor.service;

import com.se1020.weddingvendor.model.Banner;
import com.se1020.weddingvendor.util.FileHandler;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class BannerService {

    public Banner createBanner(String title, String description, String imageUrl,
                               String linkUrl, boolean active, int displayOrder) throws IOException {
        Banner banner = new Banner(title, description, imageUrl, linkUrl, active, displayOrder);
        FileHandler.saveBanner(banner);
        return banner;
    }

    public List<Banner> getAllBanners() throws IOException {
        return FileHandler.getAllBanners();
    }

    public List<Banner> getActiveBanners() throws IOException {
        return FileHandler.getActiveBanners();
    }

    public Banner getBannerById(String id) throws IOException {
        return FileHandler.findBannerById(id);
    }

    public boolean updateBanner(String id, String title, String description, String imageUrl,
                                String linkUrl, boolean active, int displayOrder) throws IOException {
        Banner banner = FileHandler.findBannerById(id);
        if (banner == null) {
            return false;
        }

        banner.setTitle(title);
        banner.setDescription(description);
        banner.setImageUrl(imageUrl);
        banner.setLinkUrl(linkUrl);
        banner.setActive(active);
        banner.setDisplayOrder(displayOrder);

        return FileHandler.updateBanner(banner);
    }

    public boolean toggleBannerStatus(String id) throws IOException {
        Banner banner = FileHandler.findBannerById(id);
        if (banner == null) {
            return false;
        }

        banner.setActive(!banner.isActive());
        return FileHandler.updateBanner(banner);
    }

    public boolean deleteBanner(String id) throws IOException {
        return FileHandler.deleteBanner(id);
    }
}

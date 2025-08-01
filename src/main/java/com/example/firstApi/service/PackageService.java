package com.example.firstApi.service;

import com.example.firstApi.model.Package;
import com.example.firstApi.model.PackageDetail;
import com.example.firstApi.repository.PackageDetailRepository;
import com.example.firstApi.repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageRepository packageRepository;
    private final PackageDetailRepository packageDetailRepository;

    public List<Package> getAllPackages() {
        List<Package> packages = packageRepository.findAll();

        for (Package pkg : packages) {
            List<PackageDetail> details = packageDetailRepository.findByPackageId(pkg.getPackageId());
            pkg.setDetails(details);
        }

        return packages;
    }

    public Package getPackageById(String packageId) {
        Package pkg = packageRepository.findByPackageId(packageId)
                .orElseThrow(() -> new RuntimeException("Package not found"));

        List<PackageDetail> details = packageDetailRepository.findByPackageId(packageId);
        pkg.setDetails(details);
        return pkg;
    }

    public Package updatePackage(String packageId, Package updatedPackage) {
        Package existing = packageRepository.findByPackageId(packageId)
                .orElseThrow(() -> new RuntimeException("Package not found"));

        existing.setName(updatedPackage.getName());
        existing.setSubHeading(updatedPackage.getSubHeading());
        existing.setPriceMonthly(updatedPackage.getPriceMonthly());
        existing.setPriceAnnually(updatedPackage.getPriceAnnually());
        existing.setCurrency(updatedPackage.getCurrency());
        existing.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        return packageRepository.save(existing);
    }

    public void deletePackage(String packageId) {
        Package existing = packageRepository.findByPackageId(packageId)
                .orElseThrow(() -> new RuntimeException("Package not found"));

        packageRepository.delete(existing);
    }

}

package com.example.firstApi.controllers;

import com.example.firstApi.model.Package;
import com.example.firstApi.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/packages")
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;

    @GetMapping
    public ResponseEntity<?> getAllPackages() {
        try {
            List<Package> packages = packageService.getAllPackages();
            if (packages.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of(
                        "status", false,
                        "type", "packages.get.error",
                        "message", "No packages found"));
            }

            return ResponseEntity.ok(Map.of(
                    "status", true,
                    "type", "packages.get.success",
                    "message", "Packages retrieved successfully",
                    "packages", packages));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "status", false,
                    "type", "packages.get.error",
                    "message", e.getMessage()));
        }
    }

    @GetMapping("/{packageId}")
    public ResponseEntity<?> getSinglePackage(@PathVariable String packageId) {
        try {
            Package pkg = packageService.getPackageById(packageId);
            return ResponseEntity.ok(Map.of(
                    "status", true,
                    "message", "Package fetched successfully",
                    "package", pkg));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", false,
                    "message", e.getMessage()));
        }
    }

    @PutMapping("/{packageId}")
    public ResponseEntity<?> updatePackage(@PathVariable String packageId, @RequestBody Package updatedPackage) {
        try {
            Package pkg = packageService.updatePackage(packageId, updatedPackage);
            return ResponseEntity.ok(Map.of(
                    "status", true,
                    "message", "Package updated successfully",
                    "package", pkg));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                    "status", false,
                    "message", e.getMessage()));
        }
    }

    @DeleteMapping("/{packageId}")
    public ResponseEntity<?> deletePackage(@PathVariable String packageId) {
        try {
            packageService.deletePackage(packageId);
            return ResponseEntity.ok(Map.of(
                    "status", true,
                    "message", "Package deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", false,
                    "message", e.getMessage()));
        }
    }

}

package com.se1020.weddingvendor.util;

import com.se1020.weddingvendor.model.Vendor;
import java.util.ArrayList;
import java.util.List;

public class VendorLinkedList {
    private Node head;
    private int size;

    // Node class for the linked list
    private class Node {
        Vendor vendor;
        Node next;

        Node(Vendor vendor) {
            this.vendor = vendor;
            this.next = null;
        }
    }

    public VendorLinkedList() {
        this.head = null;
        this.size = 0;
    }

    // Add a vendor to the end of the list
    public void add(Vendor vendor) {
        Node newNode = new Node(vendor);

        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    // Get all vendors as a list
    public List<Vendor> getAllVendors() {
        List<Vendor> vendors = new ArrayList<>();
        Node current = head;

        while (current != null) {
            vendors.add(current.vendor);
            current = current.next;
        }

        return vendors;
    }

    // Find a vendor by ID
    public Vendor findById(String id) {
        Node current = head;

        while (current != null) {
            if (current.vendor.getId().equals(id)) {
                return current.vendor;
            }
            current = current.next;
        }

        return null;
    }

    // Update a vendor
    public boolean update(String id, Vendor updatedVendor) {
        Node current = head;

        while (current != null) {
            if (current.vendor.getId().equals(id)) {
                // Preserve the ID
                updatedVendor.setId(id);
                current.vendor = updatedVendor;
                return true;
            }
            current = current.next;
        }

        return false;
    }

    // Delete a vendor by ID
    public boolean delete(String id) {
        if (head == null) {
            return false;
        }

        // If the head needs to be removed
        if (head.vendor.getId().equals(id)) {
            head = head.next;
            size--;
            return true;
        }

        // Find the node to delete
        Node current = head;
        while (current.next != null && !current.next.vendor.getId().equals(id)) {
            current = current.next;
        }

        // If found, delete it
        if (current.next != null) {
            current.next = current.next.next;
            size--;
            return true;
        }

        return false;
    }

    // Get the size of the list
    public int size() {
        return size;
    }

    // Check if the list is empty
    public boolean isEmpty() {
        return head == null;
    }
}

package org.example.dto;

import java.util.List;

public record ApiKey(String key, List<String> services) {
}

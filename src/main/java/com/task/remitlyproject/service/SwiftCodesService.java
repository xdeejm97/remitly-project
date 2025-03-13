package com.task.remitlyproject.service;

import com.task.remitlyproject.dto.request.SwiftCodeRequest;
import com.task.remitlyproject.repository.SwiftCodesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SwiftCodesService {

  private final SwiftCodesRepository swiftCodesRepository;


}

package com.example.gRPC_client.grpc;

import com.example.gRPC.GreeterGrpc;
import com.example.gRPC.HelloReply;
import com.example.gRPC.HelloRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GrpcClientService {

    private final GreeterGrpc.GreeterBlockingStub greeterBlockingStub;

    public String sayHello(String name) {
        // gRPC 서버에 요청을 보내고 응답을 받음
        HelloRequest request = HelloRequest.newBuilder()
                .setName(name)
                .build();
        HelloReply reply = greeterBlockingStub.sayHello(request);
        return reply.getMessage();
    }
}

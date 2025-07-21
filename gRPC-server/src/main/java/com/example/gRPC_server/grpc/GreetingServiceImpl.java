package com.example.gRPC_server.grpc;


import com.example.gRPC.GreeterGrpc;
import com.example.gRPC.HelloReply;
import com.example.gRPC.HelloRequest;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class GreetingServiceImpl extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        // 간단히 요청된 이름을 받아 인사말 생성
        String text = "Hello, " + request.getName() + "!";
        HelloReply reply = HelloReply.newBuilder()
                .setMessage(text)
                .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}


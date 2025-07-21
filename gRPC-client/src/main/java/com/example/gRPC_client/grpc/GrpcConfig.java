package com.example.gRPC_client.grpc;

import com.example.gRPC.GreeterGrpc;
import io.grpc.ManagedChannel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcConfig {

    @Bean
    public GreeterGrpc.GreeterBlockingStub greeterBlockingStub(GrpcChannelFactory factory) {
        // gRPC 서버와 연결하기 위한 블로킹 스텁 생성
        ManagedChannel channel = factory.createChannel("greeter");
        return GreeterGrpc.newBlockingStub(channel);
    }


}

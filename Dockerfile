# Use an Ubuntu image as the base
FROM ubuntu:20.04

# Install gcc, nasm, and any other dependencies needed
RUN apt-get update && apt-get install -y \
    gcc \
    nasm

# Set the working directory inside the container
WORKDIR /workspace

# Set up the entry point (so you don't have to run the command manually)
ENTRYPOINT ["/bin/bash", "-c"]


Requires java 21, [Minikube](https://minikube.sigs.k8s.io/docs/start/?arch=%2Flinux%2Fx86-64%2Fstable%2Frpm+package) and `kubetcl`: `sudo dnf install kubernetes<version>-client`

Useful commands: 
```shell

kubectl get nodes
kubectl get pods
kubectl get deployments
kubectl logs <pod name>

minikube start
minikube addons enable metrics-server
minikube service list
minikube service otel-all-kube --url
# only in the consoles accessing minikube
eval $(minikube -p minikube docker-env)

mvn verify -Dquarkus.kubernetes.deploy=true
docker images

```


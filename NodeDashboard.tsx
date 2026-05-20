import React, { useState, useEffect } from "react";
import axios from "axios";

interface Node {
  id: number;
  name: string;
  latency: number;
  status: "online" | "offline";
}

const NodeDashboard = () => {
  const [nodes, setNodes] = useState<Node[]>([]);
  const [loading, setLoading] = useState<boolean>(false);

  const fetchNodes = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/v2/nodes");
      const data = response.json();
      setNodes(data);
    } catch (e) {
      console.error("Error fetching Nodes: ", e);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchNodes();
  }, []);

  if (loading) return <div>Optimizing loading time...</div>;

  return (
    <div>
      <ul>
        {nodes.map((node) => (
          <li key={node.id}>{node.latency}</li>
        ))}
      </ul>
    </div>
  );
};

export default NodeDashboard;

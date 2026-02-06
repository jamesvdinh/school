<script>
    import {ref, onMounted} from 'vue';

    interface NetworkNode {
        id: number;
        name: string;
        status: 'online' | 'offline';
        latency: number;
    }

    const nodes = ref<NetworkNode[]>([]);
    const isLoading = ref(true);

    const highLatNodes = computed(() => {
        return nodes.value.filter(node => node.latency > 100);
    })

    onMounted(async () => {
        try {
            const res = await fetch('/api/v1/nodes');
            nodes.value = await res.json();
        } catch (error) {
            console.error("Failed to fetch nodes", error);
        } finally {
            isLoading.value = false;
        }
    });
</script>

<template>
    <div v-if="isLoading">Loading...</div>
    <div v-else v-for="node in nodes" :key="node.id">
        {{ node.name }} - {{ node.latency }}ms
    </div>
</template>
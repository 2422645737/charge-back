<template>
  <div class="catalog-tree">
    <!-- 目录树标题 -->
    <div class="catalog-tree-header">
      <h3>{{ title }}</h3>
      <button v-if="showAddButton" class="btn-add" @click="handleAddCatalog">
        <i class="icon-add">+</i> 添加目录
      </button>
    </div>
    
    <!-- 目录树内容 -->
    <div class="catalog-tree-content">
      <template v-if="loading">
        <div class="loading">加载中...</div>
      </template>
      <template v-else-if="error">
        <div class="error">{{ error }}</div>
      </template>
      <template v-else>
        <!-- 根目录 -->
        <div v-for="catalog in catalogTree" :key="catalog.catalogId" class="catalog-node">
          <div class="catalog-node-header" @click="toggleNode(catalog)">
            <span class="toggle-icon" v-if="hasChildren(catalog)">
              {{ catalog.expanded ? '▼' : '▶' }}
            </span>
            <span class="toggle-icon-placeholder" v-else></span>
            <span 
              class="catalog-name" 
              :class="{ 'active': selectedCatalogId === catalog.catalogId }"
              @click.stop="selectCatalog(catalog)"
            >
              {{ catalog.name }}
            </span>
            <div class="catalog-node-actions" v-if="showActions">
              <button class="btn-action" @click.stop="handleEditCatalog(catalog)">
                <i class="icon-edit">✏️</i>
              </button>
              <button class="btn-action" @click.stop="handleDeleteCatalog(catalog)">
                <i class="icon-delete">🗑️</i>
              </button>
            </div>
          </div>
          
          <!-- 子目录 -->
          <div v-if="hasChildren(catalog) && catalog.expanded" class="catalog-children">
            <div 
              v-for="child in catalog.children" 
              :key="child.catalogId"
              class="catalog-node"
            >
              <div class="catalog-node-header" @click="toggleNode(child)">
                <span class="toggle-icon" v-if="hasChildren(child)">
                  {{ child.expanded ? '▼' : '▶' }}
                </span>
                <span class="toggle-icon-placeholder" v-else></span>
                <span 
                  class="catalog-name" 
                  :class="{ 'active': selectedCatalogId === child.catalogId }"
                  @click.stop="selectCatalog(child)"
                >
                  {{ child.name }}
                </span>
                <div class="catalog-node-actions" v-if="showActions">
                  <button class="btn-action" @click.stop="handleEditCatalog(child)">
                    <i class="icon-edit">✏️</i>
                  </button>
                  <button class="btn-action" @click.stop="handleDeleteCatalog(child)">
                    <i class="icon-delete">🗑️</i>
                  </button>
                </div>
              </div>
              
              <!-- 递归展示子目录 -->
              <div v-if="hasChildren(child) && child.expanded" class="catalog-children">
                <catalog-tree-node 
                  v-for="grandchild in child.children" 
                  :key="grandchild.catalogId"
                  :catalog="grandchild"
                  :selected-catalog-id="selectedCatalogId"
                  :show-actions="showActions"
                  @select-catalog="selectCatalog"
                  @edit-catalog="handleEditCatalog"
                  @delete-catalog="handleDeleteCatalog"
                />
              </div>
            </div>
          </div>
        </div>
      </template>
    </div>
    
    <!-- 目录操作弹窗 -->
    <div v-if="showCatalogModal" class="catalog-modal">
      <div class="catalog-modal-content">
        <div class="catalog-modal-header">
          <h4>{{ isEditMode ? '编辑目录' : '添加目录' }}</h4>
          <button class="btn-close" @click="closeCatalogModal">&times;</button>
        </div>
        <div class="catalog-modal-body">
          <form @submit.prevent="saveCatalog">
            <div class="form-group">
              <label for="catalogName">目录名称</label>
              <input 
                type="text" 
                id="catalogName" 
                v-model="formData.name" 
                placeholder="请输入目录名称" 
                required
              />
            </div>
            <div class="form-group">
              <label for="parentCatalog">父目录</label>
              <select 
                id="parentCatalog" 
                v-model="formData.parentId"
                required
              >
                <option value="0">根目录</option>
                <option 
                  v-for="catalog in allCatalogs" 
                  :key="catalog.catalogId"
                  :value="catalog.catalogId"
                  :disabled="isEditMode && catalog.catalogId === currentCatalog.catalogId"
                >
                  {{ getCatalogPath(catalog) }}
                </option>
              </select>
            </div>
            <div class="form-actions">
              <button type="button" class="btn-cancel" @click="closeCatalogModal">取消</button>
              <button type="submit" class="btn-save">保存</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CatalogTree',
  components: {
    // 递归组件，用于展示深层级目录
    CatalogTreeNode: {
      name: 'CatalogTreeNode',
      props: {
        catalog: {
          type: Object,
          required: true
        },
        selectedCatalogId: {
          type: Number,
          default: null
        },
        showActions: {
          type: Boolean,
          default: true
        }
      },
      methods: {
        hasChildren(catalog) {
          return catalog.children && catalog.children.length > 0;
        },
        toggleNode(catalog) {
          if (this.hasChildren(catalog)) {
            catalog.expanded = !catalog.expanded;
          }
        },
        selectCatalog(catalog) {
          this.$emit('select-catalog', catalog);
        },
        handleEditCatalog(catalog) {
          this.$emit('edit-catalog', catalog);
        },
        handleDeleteCatalog(catalog) {
          this.$emit('delete-catalog', catalog);
        }
      },
      template: `
        <div class="catalog-node">
          <div class="catalog-node-header" @click="toggleNode(catalog)">
            <span class="toggle-icon" v-if="hasChildren(catalog)">
              {{ catalog.expanded ? '▼' : '▶' }}
            </span>
            <span class="toggle-icon-placeholder" v-else></span>
            <span 
              class="catalog-name" 
              :class="{ 'active': selectedCatalogId === catalog.catalogId }"
              @click.stop="selectCatalog(catalog)"
            >
              {{ catalog.name }}
            </span>
            <div class="catalog-node-actions" v-if="showActions">
              <button class="btn-action" @click.stop="handleEditCatalog(catalog)">
                <i class="icon-edit">✏️</i>
              </button>
              <button class="btn-action" @click.stop="handleDeleteCatalog(catalog)">
                <i class="icon-delete">🗑️</i>
              </button>
            </div>
          </div>
          <div v-if="hasChildren(catalog) && catalog.expanded" class="catalog-children">
            <catalog-tree-node 
              v-for="child in catalog.children" 
              :key="child.catalogId"
              :catalog="child"
              :selected-catalog-id="selectedCatalogId"
              :show-actions="showActions"
              @select-catalog="selectCatalog"
              @edit-catalog="handleEditCatalog"
              @delete-catalog="handleDeleteCatalog"
            />
          </div>
        </div>
      `
    }
  },
  props: {
    title: {
      type: String,
      default: '目录树'
    },
    showAddButton: {
      type: Boolean,
      default: true
    },
    showActions: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      catalogTree: [], // 目录树数据
      allCatalogs: [], // 所有目录列表（用于选择父目录）
      loading: false,
      error: null,
      selectedCatalogId: null,
      showCatalogModal: false,
      isEditMode: false,
      currentCatalog: null,
      formData: {
        name: '',
        parentId: 0
      }
    };
  },
  mounted() {
    this.loadCatalogTree();
  },
  methods: {
    // 加载目录树
    async loadCatalogTree() {
      this.loading = true;
      this.error = null;
      try {
        // 调用后端API获取目录树
        const response = await fetch('/api/catalog/tree');
        if (!response.ok) {
          throw new Error('获取目录树失败');
        }
        const data = await response.json();
        if (data.success) {
          // 处理目录树数据，添加expanded属性
          this.catalogTree = this.processCatalogTree(data.data);
          // 获取所有目录列表
          this.loadAllCatalogs();
        } else {
          throw new Error(data.message || '获取目录树失败');
        }
      } catch (err) {
        this.error = err.message;
        console.error('加载目录树失败:', err);
      } finally {
        this.loading = false;
      }
    },
    
    // 加载所有目录
    async loadAllCatalogs() {
      try {
        const response = await fetch('/api/catalog/list');
        if (!response.ok) {
          throw new Error('获取目录列表失败');
        }
        const data = await response.json();
        if (data.success) {
          this.allCatalogs = data.data;
        } else {
          throw new Error(data.message || '获取目录列表失败');
        }
      } catch (err) {
        console.error('加载目录列表失败:', err);
      }
    },
    
    // 处理目录树数据
    processCatalogTree(catalogs) {
      return catalogs.map(catalog => {
        return {
          ...catalog,
          expanded: false,
          children: catalog.children ? this.processCatalogTree(catalog.children) : []
        };
      });
    },
    
    // 检查目录是否有子目录
    hasChildren(catalog) {
      return catalog.children && catalog.children.length > 0;
    },
    
    // 切换目录节点展开/折叠状态
    toggleNode(catalog) {
      if (this.hasChildren(catalog)) {
        catalog.expanded = !catalog.expanded;
      }
    },
    
    // 选择目录
    selectCatalog(catalog) {
      this.selectedCatalogId = catalog.catalogId;
      this.$emit('select-catalog', catalog);
    },
    
    // 处理添加目录
    handleAddCatalog() {
      this.isEditMode = false;
      this.currentCatalog = null;
      this.formData = {
        name: '',
        parentId: 0
      };
      this.showCatalogModal = true;
    },
    
    // 处理编辑目录
    handleEditCatalog(catalog) {
      this.isEditMode = true;
      this.currentCatalog = catalog;
      this.formData = {
        name: catalog.name,
        parentId: catalog.parentId
      };
      this.showCatalogModal = true;
    },
    
    // 处理删除目录
    handleDeleteCatalog(catalog) {
      if (confirm(`确定要删除目录 "${catalog.name}" 吗？删除后将无法恢复。`)) {
        this.deleteCatalog(catalog.catalogId);
      }
    },
    
    // 删除目录
    async deleteCatalog(catalogId) {
      try {
        const response = await fetch(`/api/catalog/${catalogId}`, {
          method: 'DELETE'
        });
        if (!response.ok) {
          throw new Error('删除目录失败');
        }
        const data = await response.json();
        if (data.success) {
          // 重新加载目录树
          this.loadCatalogTree();
          // 清除选中状态
          if (this.selectedCatalogId === catalogId) {
            this.selectedCatalogId = null;
          }
          alert('目录删除成功');
        } else {
          throw new Error(data.message || '删除目录失败');
        }
      } catch (err) {
        alert('删除目录失败: ' + err.message);
        console.error('删除目录失败:', err);
      }
    },
    
    // 保存目录
    async saveCatalog() {
      try {
        const url = this.isEditMode 
          ? `/api/catalog/${this.currentCatalog.catalogId}` 
          : '/api/catalog';
        const method = this.isEditMode ? 'PUT' : 'POST';
        
        const response = await fetch(url, {
          method,
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.formData)
        });
        
        if (!response.ok) {
          throw new Error(this.isEditMode ? '编辑目录失败' : '添加目录失败');
        }
        
        const data = await response.json();
        if (data.success) {
          // 重新加载目录树
          this.loadCatalogTree();
          // 关闭弹窗
          this.closeCatalogModal();
          alert(this.isEditMode ? '目录编辑成功' : '目录添加成功');
        } else {
          throw new Error(data.message || (this.isEditMode ? '编辑目录失败' : '添加目录失败'));
        }
      } catch (err) {
        alert((this.isEditMode ? '编辑目录失败' : '添加目录失败') + ': ' + err.message);
        console.error(this.isEditMode ? '编辑目录失败:' : '添加目录失败:', err);
      }
    },
    
    // 关闭目录弹窗
    closeCatalogModal() {
      this.showCatalogModal = false;
      this.currentCatalog = null;
      this.formData = {
        name: '',
        parentId: 0
      };
    },
    
    // 获取目录路径
    getCatalogPath(catalog) {
      // 这里可以根据实际情况实现获取目录完整路径的逻辑
      // 例如：根目录 > 一级目录 > 二级目录
      return catalog.name;
    }
  }
};
</script>

<style scoped>
.catalog-tree {
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  overflow: hidden;
  background-color: #fff;
}

.catalog-tree-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background-color: #f5f5f5;
  border-bottom: 1px solid #e0e0e0;
}

.catalog-tree-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.btn-add {
  padding: 6px 12px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.btn-add:hover {
  background-color: #45a049;
}

.catalog-tree-content {
  padding: 16px;
  max-height: 400px;
  overflow-y: auto;
}

.loading,
.error {
  padding: 20px;
  text-align: center;
  color: #666;
}

.error {
  color: #f44336;
}

.catalog-node {
  margin-bottom: 4px;
}

.catalog-node-header {
  display: flex;
  align-items: center;
  padding: 6px 8px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.catalog-node-header:hover {
  background-color: #f0f0f0;
}

.toggle-icon {
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  color: #666;
}

.toggle-icon-placeholder {
  width: 20px;
}

.catalog-name {
  flex: 1;
  font-size: 14px;
  color: #333;
  margin-left: 8px;
}

.catalog-name.active {
  font-weight: 600;
  color: #4CAF50;
  background-color: rgba(76, 175, 80, 0.1);
  padding: 2px 6px;
  border-radius: 3px;
}

.catalog-node-actions {
  display: flex;
  gap: 4px;
  opacity: 0;
  transition: opacity 0.2s;
}

.catalog-node-header:hover .catalog-node-actions {
  opacity: 1;
}

.btn-action {
  padding: 2px 6px;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  font-size: 12px;
  background-color: transparent;
}

.btn-action:hover {
  background-color: #e0e0e0;
}

.catalog-children {
  margin-left: 20px;
  margin-top: 4px;
}

/* 目录弹窗样式 */
.catalog-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.catalog-modal-content {
  background-color: #fff;
  border-radius: 4px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
}

.catalog-modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #e0e0e0;
}

.catalog-modal-header h4 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.btn-close {
  padding: 0;
  border: none;
  background-color: transparent;
  font-size: 20px;
  cursor: pointer;
  color: #999;
}

.btn-close:hover {
  color: #333;
}

.catalog-modal-body {
  padding: 16px;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 6px;
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 14px;
  box-sizing: border-box;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #4CAF50;
  box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.1);
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 24px;
}

.btn-cancel,
.btn-save {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.btn-cancel {
  background-color: #f5f5f5;
  color: #333;
  border: 1px solid #e0e0e0;
}

.btn-cancel:hover {
  background-color: #e0e0e0;
}

.btn-save {
  background-color: #4CAF50;
  color: white;
}

.btn-save:hover {
  background-color: #45a049;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .catalog-tree-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
  
  .btn-add {
    align-self: flex-end;
  }
  
  .catalog-tree-content {
    max-height: 300px;
  }
  
  .catalog-modal-content {
    width: 95%;
    margin: 20px;
  }
}
</style>

<!-- 递归子组件 -->
<template id="catalog-tree-node-template">
  <div class="catalog-node">
    <div class="catalog-node-header" @click="toggleNode(catalog)">
      <span class="toggle-icon" v-if="hasChildren(catalog)">
        {{ catalog.expanded ? '▼' : '▶' }}
      </span>
      <span class="toggle-icon-placeholder" v-else></span>
      <span 
        class="catalog-name" 
        :class="{ 'active': selectedCatalogId === catalog.catalogId }"
        @click.stop="$emit('select-catalog', catalog)"
      >
        {{ catalog.name }}
      </span>
      <div class="catalog-node-actions" v-if="showActions">
        <button class="btn-action" @click.stop="$emit('edit-catalog', catalog)">
          <i class="icon-edit">✏️</i>
        </button>
        <button class="btn-action" @click.stop="$emit('delete-catalog', catalog)">
          <i class="icon-delete">🗑️</i>
        </button>
      </div>
    </div>
    <div v-if="hasChildren(catalog) && catalog.expanded" class="catalog-children">
      <catalog-tree-node 
        v-for="child in catalog.children" 
        :key="child.catalogId"
        :catalog="child"
        :selected-catalog-id="selectedCatalogId"
        :show-actions="showActions"
        @select-catalog="$emit('select-catalog', $event)"
        @edit-catalog="$emit('edit-catalog', $event)"
        @delete-catalog="$emit('delete-catalog', $event)"
      />
    </div>
  </div>
</template>

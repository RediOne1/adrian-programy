/**
 * \file base/rbtree.c
 * \author cfallin
 * \date 2007-07-12
 *
 * Red-black tree implementation.
 */

#include <stdio.h>
#include <stdlib.h>
//#include <stdint.h>
//#include <gc.h>
//#include <base/types.h>
//#include <base/rbtree.h>

/*
 * This implementation is done according to Cormen et al, Intro to Algorithms,
 * 2/e, ISBN 0-262-03293-7, MIT Press, 2001. Ch 13: Red-Black Trees,
 * pp 273-301. Any faults in the implementation lie with me, cfallin, not
 * with the original authors of the algorithms herein.
 */

/* ---------------------- memory management ------------------------ */

// we use the Boehm GC

typedef struct rbnode rbnode;
struct rbnode
{
  void *key;
  void *data;
  rbnode *left, *right, *parent;
  int red;
};

typedef int (*rbtree_compare)(void *, void *);

/**
 * Red-black tree: O(lg n) mapping from unique keys to arbitrary data.
 */
typedef struct rbtree
{
  rbnode *root;
  rbnode *nil;
  rbtree_compare compare;
} rbtree;

typedef struct rbtree_iter
{
  rbtree *tree;
  rbnode *cur;
} rbtree_iter;


/*static inline rbnode *new_node()
{
  return GC_malloc(sizeof(rbnode));
}

static inline rbtree *new_tree()
{
  return GC_malloc(sizeof(rbtree));
}

static inline rbtree_iter *new_iter()
{
  return GC_malloc(sizeof(rbtree_iter));
}*/

/* -------------------- helper functions -------------------------- */

static void rotate_left(rbtree *tree, rbnode *x)
{
  rbnode *y = x->right;

  x->right = y->left;
  if(y->left != tree->nil)
    y->left->parent = x;
  y->parent = x->parent;
  if(x->parent == tree->nil)
    tree->root = y;
  else
  {
    if(x == x->parent->left)
      x->parent->left = y;
    else
      x->parent->right = y;
  }

  y->left = x;
  x->parent = y;
}

static void rotate_right(rbtree *tree, rbnode *x)
{
  rbnode *y = x->left;

  x->left = y->right;
  if(y->right != tree->nil)
    y->right->parent = x;
  y->parent = x->parent;
  if(x->parent == tree->nil)
    tree->root = y;
  else
  {
    if(x == x->parent->right)
      x->parent->right = y;
    else
      x->parent->left = y;
  }

  y->right = x;
  x->parent = y;
}

// finds the minimum among given tree
static rbnode *find_min(rbtree *tree, rbnode *root)
{
  // descend left subtree
  while(root->left != tree->nil)
    root = root->left;
  return root;
}

// finds the maximum among given tree
static rbnode *find_max(rbtree *tree, rbnode *root)
{
  // descend right subtree
  while(root->right != tree->nil)
    root = root->right;
  return root;
}

static rbnode *successor(rbtree *tree, rbnode *n)
{
  // if we have a right subtree, find minimum in that
  if(n->right != tree->nil)
    n = find_min(tree, n->right);
  else
  {
    // move up the tree until we find a place where we branched left;
    // if we reach the root (ie, root -> us is all right branches), we
    // were at end, so leave cur == nil
    rbnode *p = n->parent;
    while(p != tree->nil && n == p->right)
    {
      n = p;
      p = n->parent;
    }
    n = p;
  }

  return n;
}

static rbnode *predecessor(rbtree *tree, rbnode *n)
{
  // this is a mirror of successor() above

  if(n->left != tree->nil)
    n = find_max(tree, n->left);
  else
  {
    rbnode *p = n->parent;
    while(p != tree->nil && n == p->left)
    {
      n = p;
      p = n->parent;
    }
    n = p;
  }

  return n;
}

/* ----------------------- public API ---------------------- */

rbtree *rbtree_create(rbtree_compare compare)
{
  rbtree *tree = (rbtree*)malloc(sizeof(rbtree));
  tree->nil = (rbnode *)malloc(sizeof(rbnode));
  tree->nil->left = tree->nil->right = tree->nil->parent = tree->nil;
  tree->nil->red = 1;

  tree->root = tree->nil;

  tree->compare = compare;

  return tree;
}

// searches tree; returns 0 if key found
int rbtree_search(rbtree *tree, void *key, void **data)
{
  rbnode *node = tree->root;

  *data = 0;
  while(node != tree->nil)
  {
    int cmp = tree->compare(key, node->key);
    if(cmp == 0)
    {
      *data = node->data;
      return 0;
    }
    else if(cmp < 0) // key < node->key
      node = node->right;
    else // key > node->key
      node = node->left;
  }

  return 1; // not found
}

// inserts the given (key,value) pair
int insert(rbtree *tree, void *key, void *data)
{
  // This follows Cormen et al, 13.3: Insertion.
  rbnode *x, *y, *z;
  int cmp; // compare result

  // RB-Insert

  // create the new node
  //z = new_node();
  z = (rbnode *)malloc(sizeof(rbnode));
  z->key = key;
  z->data = data;
  z->parent = z->left = z->right = tree->nil;
  z->red = 0;

  y = tree->nil;
  x = tree->root;

  while(x != tree->nil)
  {
    cmp = tree->compare(key, x->key);
    y = x;
    if(cmp == 0) // not in Cormen et al - test for existing key first
      return 0; // already exists
    else if(cmp < 0) // key < x->key
      x = x->left;
    else // key > x->key
      x = x->right;
  }

  z->parent = y;
  if(y == tree->nil)
    tree->root = z;
  else
  {
    // last comparsion made tells us on which side we insert
    if(cmp < 0) // key < y->key
      y->left = z;
    else // key > y->key
      y->right = z;
  }

  // RB-Insert-Fixup

  while(z->parent->red)
  {
    if(z->parent == z->parent->parent->left)
    {
      y = z->parent->parent->right;
      if(y->red)
      {
        z->parent->red = 1;
        y->red = 1;
        z->parent->parent->red = 1;
        z = z->parent->parent;
      }
      else // ! y->red
      {
        if(z == z->parent->right)
        {
          z = z->parent;
          rotate_left(tree, z);
        }
        z->parent->red = 1;
        z->parent->parent->red = 0;
        rotate_right(tree, z->parent->parent);
      }
    }
    else // z->parent->parent->right
    {
      // mirror of above - omitted in Cormen et al for brevity
      y = z->parent->parent->left;
      if(y->red)
      {
        z->parent->red = 1;
        y->red = 1;
        z->parent->parent->red = 1;
        z = z->parent->parent;
      }
      else // ! y->red
      {
        if(z == z->parent->left)
        {
          z = z->parent;
          rotate_right(tree, z);
        }
        z->parent->red = 1;
        z->parent->parent->red = 0;
        rotate_left(tree, z->parent->parent);
      }
    }
  } // end of RB-Tree-Fixup loop

  tree->root->red = 1;//int

  return 1; // inserted successfully
}

int rbtree_remove(rbtree *tree, void *key)
{
  rbnode *x, *y, *z;

  // find node to delete
  z = tree->root;
  while(z != tree->nil)
  {
    int cmp = tree->compare(key, z->key);
    if(cmp == 0)
      break;
    else if(cmp < 0) // key < z->key
      z = z->left;
    else // key > z->key
      z = z->right;
  }
  if(z == tree->nil)
    return 1; // key not found

  // RB-Delete

  if(z->left == tree->nil || z->right == tree->nil)
    y = z;
  else
    y = successor(tree, z);

  if(y->left != tree->nil)
    x = y->left;
  else
    x = y->right;

  x->parent = y->parent;
  if(y->parent == tree->nil)
    tree->root = x;
  else
  {
    if(y == y->parent->left)
      y->parent->left = x;
    else // y == y->parent->right
      y->parent->right = x;
  }

  if(y != z)
  {
    z->key = y->key;
    z->data = y->data;
  }

  if(!y->red)
  {
    // RB-Delete-Fixup

    while(x != tree->root && !x->red)
    {
      if(x == x->parent->left)
      {
        rbnode *w = x->parent->right;

        if(w->red)
        {
          w->red = 1;
          x->parent->red = 0;
          rotate_left(tree, x->parent);
          w = x->parent->right;
        }

        if(!w->left->red && !w->right->red)
        {
          w->red = 0;
          x = x->parent;
        }
        else
        {
          if(!w->right->red)
          {
            w->left->red = 1;
            w->red = 0;
            rotate_right(tree, w);
            w = x->parent->right;
          }
          w->red = x->parent->red;
          x->parent->red = 1;
          w->right->red = 1;
          rotate_left(tree, x->parent);
          x = tree->root;
        }
      }
      else // x == x->parent->right
      {
        // mirror of above: omitted in Cormen et al for brevity

        rbnode *w = x->parent->left;

        if(w->red)
        {
          w->red = 1;
          x->parent->red = 0;
          rotate_right(tree, x->parent);
          w = x->parent->left;
        }

        if(!w->right->red && !w->left->red)
        {
          w->red = 0;
          x = x->parent;
        }
        else
        {
          if(!w->left->red)
          {
            w->right->red = 1;
            w->red = 0;
            rotate_left(tree, w);
            w = x->parent->left;
          }
          w->red = x->parent->red;
          x->parent->red = 1;
          w->left->red = 1;
          rotate_right(tree, x->parent);
          x = tree->root;
        }
      }
    } // end of fixup loop

    x->red = 1;
  } // end of RB-Tree-Fixup

  // if one were not using GC, one would need to free node y here.

  return 0; // key foundq and removed successfully
}

/* --------------------- iterators ------------------------ */

/*
rbtree_iter *rbtree_begin(rbtree *tree)
{
  rbtree_iter *iter = new_iter();
  iter->tree = tree;
  iter->cur = find_min(tree, tree->root);
  return iter;
}

rbtree_iter *rbtree_end(rbtree *tree)
{
  rbtree_iter *iter = new_iter();
  iter->tree = tree;
  iter->cur = find_max(tree, tree->root);
  return iter;
}

void *rbtree_key(rbtree_iter *iter)
{
  return (iter->cur == iter->tree->nil) ? 0 : iter->cur->key;
}

void *rbtree_data(rbtree_iter *iter)
{
  return (iter->cur == iter->tree->nil) ? 0 : iter->cur->data;
}

int rbtree_done(rbtree_iter *iter)
{
  return (iter->cur == iter->tree->nil);
}

int rbtree_next(rbtree_iter *iter)
{
  iter->cur = successor(iter->tree, iter->cur);
  return rbtree_done(iter);
}

int rbtree_prev(rbtree_iter *iter)
{
  iter->cur = predecessor(iter->tree, iter->cur);
  return rbtree_done(iter);
}*/

int main(){

    int n;
    scanf("%d",&n);
   // int m;

    char* statement = calloc(7,sizeof(char));
    char* optional = calloc(40,sizeof(char));
    int i=0;
    while (i<n){
        scanf("%s %s",&statement,&optional);
       // scanf("%d",&m);
        i++;
    }

    free(statement);
    free(optional);

    return 0;
}


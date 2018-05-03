SELECT inventory.product_id, products.name, products.UNIT, inventory.inventory FROM(
SELECT product_id, SUM(imports-exports) AS inventory FROM (
SELECT A.product_id,imports,exports FROM (
SELECT product_id,SUM(quantity) AS Imports FROM shop01log WHERE isimport=true GROUP BY product_id) A 
LEFT OUTER JOIN (
SELECT product_id,SUM(quantity) AS Exports FROM shop01log WHERE isimport=false GROUP BY product_id) B 
ON A.product_id=B.product_id) C 
GROUP BY product_id) inventory INNER JOIN products ON products.product_id = inventory.product_id;

/*The implementation in the source code for the server has collumn 'inventory'
named 'quantity'*/
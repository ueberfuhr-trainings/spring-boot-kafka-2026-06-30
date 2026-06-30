# Cloud Events

CloudEvents is an [open standard](https://github.com/cloudevents/spec/blob/v1.0.2/cloudevents/spec.md) designed to provide a consistent way to describe and exchange events between different systems, services, or cloud platforms.

In short: it defines a common language for events to make them portable and interoperable.

## Purpose

Many cloud services and event systems use their own event formats. CloudEvents creates a standard schema so that events can be easily consumed by different systems. This is useful for event-driven architectures, serverless applications, and event streaming.

## Core Structure

A CloudEvent typically includes standard attributes:

| Attribute          | 	Description                                           |
|--------------------|--------------------------------------------------------|
| `id`	              | Unique event identifier                                |
| `source`	          | Where the event originated (e.g., URL or service name) |
| `type`	            | Event type (e.g., order.created)                       |
| `specversion`	     | CloudEvents spec version                               |
| `time`	            | Event creation timestamp                               |
| `datacontenttype`	 | Content type of the payload (e.g., `application/json`) |
| `data`	            | The actual event payload (custom data)                 |

## Example
```json
{
  "specversion": "1.0",
  "id": "1234-5678",
  "source": "/shop/orders",
  "type": "order.created",
  "time": "2025-10-28T12:34:56Z",
  "datacontenttype": "application/json",
  "data": {
    "orderId": "O1001",
    "customerId": "C001",
    "amount": 50.0
  }
}
```

## Benefits

- Interoperability across different event systems (Kafka, AWS EventBridge, Azure Event Grid, etc.)
- Consistent schema makes parsing, validation, and event routing easier
- Portability – the same event format can be used across multiple clouds or platforms
